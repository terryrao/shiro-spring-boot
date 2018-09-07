package com.terryrao.shiro.cache;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *
 */
public class ShiroSessionDao extends CachingSessionDAO {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 保存到Redis中key的前缀 prefix+sessionId
    public static final String SESSION_PREFIX = "shiro-activeSessionCache";

    // 设置内存会话的过期时间
    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
    // 特殊配置 只用于没有Redis时 将Session放到EhCache中
//    private Boolean onlyEhCache;

    private static final int SESSION_EXPIRE = 1800; //会话过期时间

    private static ThreadLocal<Map<Serializable, SessionInMemory>> sessionsInThread = new ThreadLocal<>();



    /*  @Override
      public Session readSession(Serializable sessionId) throws UnknownSessionException {
          Session cached = null;
          try {
              cached = super.getCachedSession(sessionId);
          } catch (Exception e) {
              logger.error(e.getMessage(),e);
          }
          if (onlyEhCache) {
              return cached;
          }

          if (cached == null || cached.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
              cached = this.doReadSession(sessionId);
              if (cached == null) {
                  throw new UnknownSessionException();
              }else  {
                  ((ShiroSession)cached).setChange(true);
                  super.update(cached);
              }
          }
          return cached;
      }
  */

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            logger.error("session is null");
            throw new UnknownSessionException("session is null");
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    /**
     * 从Redis中读取Session,并重置过期时间
     *
     * @param sessionId 会话ID
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("session id is null");
            }
            return null;
        }
        Session s = getSessionFromThreadLocal(sessionId);

        if (s != null) {
            return s;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("read session from redis");
        }
        try {
            s = super.getActiveSessionsCache().get(sessionId);
            setSessionToThreadLocal(sessionId, s);
        } catch (Exception e) {
            logger.error("read session error. sessionId =" + sessionId);
        }
        return s;

    }


    /**
     * 读取本地的会话
     */
    private Session getSessionFromThreadLocal(Serializable sessionId) {
        Session s = null;

        if (sessionsInThread.get() == null) {
            return null;
        }

        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null) {
            return null;
        }
        Date now = new Date();
        long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
        if (duration < DEFAULT_SESSION_IN_MEMORY_TIMEOUT) {
            s = sessionInMemory.getSession();
            if (logger.isDebugEnabled()) {
                logger.debug("read session from memory {}", sessionId);
            }
        } else {
            sessionMap.remove(sessionId);
        }

        return s;
    }

    /**
     * 设置本地会话
     */
    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<>();
            sessionsInThread.set(sessionMap);
        }
        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        sessionMap.put(sessionId, sessionInMemory);
    }


    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            throw new UnknownSessionException("session or session id is null");
        }
        if (SESSION_EXPIRE * 1000 < session.getTimeout()) {
            logger.debug("Redis session expire time: " + (SESSION_EXPIRE * 1000 * 1000) + " is less than Session timeout: "
                    + session.getTimeout() + " . It may cause some problems.");
        }
        super.getActiveSessionsCache().put(session.getId(), session);
    }


    @Override
    protected void doUpdate(Session session) {

    }

    @Override
    protected void doDelete(Session session) {

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return super.getActiveSessions();
    }
}
