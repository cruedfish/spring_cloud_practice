package com.giveu.guzuul.guzuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() throws ZuulException {
        return null;
    }

}
