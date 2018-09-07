package com.terryrao.shiro.cache;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.session.Session;

import java.util.Date;

/**
 * Use ThreadLocal as a temporary storage of Session, so that shiro wouldn't keep read redis several times while a request coming.
 */
@Setter
@Getter
public class SessionInMemory {

    private Session session;
    private Date createTime;

}
