package com.giveu.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Key;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class AuthHeaderFilter extends ZuulFilter {

    private final Logger logger = Logger.getLogger(AuthHeaderFilter.class);
    
    public AuthHeaderFilter() {
        super();
    }

    @Override
    public String filterType() {
        return  "pre";
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext rtx = RequestContext.getCurrentContext();
        HttpServletRequest request = rtx.getRequest();
        String token = request.getHeader("Authorization");
        if(token == null || token.length() == 0 ){
            token = System.getProperty("hai.store.token");
            logger.info("获取的token为*********************************************"+token);
            rtx.addOriginResponseHeader("Authorization",token);
        }
        return  null;
    }
}
