package com.giveu.guauthclient.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class UserAuthFilter extends AuthenticationFilter{
    @Value("${sso.server.uri}")
    private String ssoUri;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request,response);
        Session session = subject.getSession();
        return subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        WebUtils.toHttp(servletResponse).sendRedirect(ssoUri+"/sso/login");
        return false;
    }
}
