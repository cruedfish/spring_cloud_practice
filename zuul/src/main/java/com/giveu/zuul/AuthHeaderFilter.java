package com.giveu.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.log4j.Logger;

import java.security.Key;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
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
        return 1;
    }

    @Override
    public Object run() throws ZuulException {
        Key key = MacProvider.generateKey();

        String compactJws = Jwts.builder()
                .setSubject("Joe")
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return  null;
    }
}
