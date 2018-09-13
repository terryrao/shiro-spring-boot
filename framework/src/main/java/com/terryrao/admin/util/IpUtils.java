package com.terryrao.admin.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpUtils {
    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);
    private static String serverIP = "";

    /**获取本地IP（服务器）  兼容linux*/
    public static String getServerIP(){
        if(StringUtils.isNotBlank(serverIP))    return serverIP;
        synchronized(serverIP){
            if(StringUtils.isNotBlank(serverIP))    return serverIP;
            try{
                Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                InetAddress ine = null;
                while (allNetInterfaces.hasMoreElements() && serverIP.equals("")){
                    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                    if (!netInterface.isVirtual()){
                        Enumeration<?> addresses = netInterface.getInetAddresses();
                        while (addresses.hasMoreElements() && serverIP.equals("")){
                            ine = (InetAddress) addresses.nextElement();
                            if (ine != null && ine instanceof Inet4Address){
                                if (!ine.getHostAddress().equals("127.0.0.1")&& !netInterface.isVirtual()){
                                    serverIP = ine.getHostAddress().trim();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                logger.error("获取服务器IP发生异常：", e);
            }
        }
        return serverIP;
    }

    /**获取客户端IP*/
    public static String getClientIP(HttpServletRequest request){
        String ip = null;
        if(request!=null){
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return (ip==null?"":ip.trim());
    }

    /**将IP地址转换成十进制整数*/
    public static long ipToLong(String strIp) {
        if(isboolIP(strIp)){
            return -1l;
        }
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3+1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**将十进制整数转换成ip地址*/
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    /**判断是否为ip地址*/
    public static boolean isboolIP(String ipAddress){
        String ip="(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        try {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getHeader("X-Real-IP");
            else
                ip = ip.split(",")[0];
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getHeader("Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getHeader("WL-Proxy-Client-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getHeader("HTTP_CLIENT_IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
                ip = request.getRemoteAddr();
        } catch (Exception e) {
            logger.error(" getIpAddr Exception:", e);
        }
        return ip;
    }



}
