package com.giveu.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.apache.log4j.Logger;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public class VerifyTokenFilter extends ZuulFilter {
    private final Logger logger = Logger.getLogger(AuthHeaderFilter.class);

    public VerifyTokenFilter() {
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
        return 5;
    }

    @Override
    public Object run() throws ZuulException {

        return null;
    }
}
