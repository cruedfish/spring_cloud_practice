package com.giveu.guauthclient.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haistore.redis.HaiResult;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */

public class UserAuthFilter extends AccessControlFilter{


    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Value("{sso.server.uri}")
    private String loginUri;

    private final static String LOGIN_URL = "http://localhost:7009/sso/login";

    private Logger logger = Logger.getLogger(AuthenticationFilter.class);

    private String kickoutUrl; // 踢出后到的地址
    private boolean kickoutAfter = false; // 踢出之前登录的/之后登录的用户 默认false踢出之前登录的用户
    private int maxSession = 1; // 同一个帐号最大会话数 默认1
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // 设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        //必须和ehcache缓存配置中的缓存name一致
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);
        String backurl = httpServletRequest.getRequestURI();
        if(backurl.indexOf("/SSO/registry") != -1){
            return true;
        }
        WebUtils.issueRedirect(servletRequest,servletResponse, loginUri+"?backurl="+backurl);
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request,response);
        Session session = subject.getSession();
        if ((Boolean) session.getAttribute("kickout") != null
                && (Boolean) session.getAttribute("kickout") == true) {
            subject.logout();
            return false;
        }
        if(subject.isAuthenticated()){
            return true;
        }
        return false;
    }


}
