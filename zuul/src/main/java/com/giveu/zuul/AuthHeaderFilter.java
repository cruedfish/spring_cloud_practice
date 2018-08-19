package com.giveu.zuul;

import com.giveu.gucommon.mapper.UserMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class AuthHeaderFilter extends ZuulFilter {

    @Autowired
    private UserMapper userMapper;

    private final Logger logger = Logger.getLogger(ZuulApplication.class);

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
