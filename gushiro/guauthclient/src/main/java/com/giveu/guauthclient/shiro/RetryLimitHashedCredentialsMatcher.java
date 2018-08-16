package com.giveu.guauthclient.shiro;

import com.giveu.guauthclient.util.JedisClient;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Autowired
    JedisClient jedisClient;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
            String username = (String)token.getPrincipal();
            String number = jedisClient.get(username+"_number");
            if(number == null) {
                jedisClient.set(username+"_"+number,"0",1);
            }else{
                number = (Integer.parseInt(number))+1+"";
            }
            if(Integer.parseInt(number) > 5) {
                throw new ExcessiveAttemptsException();
            }
            boolean matches = super.doCredentialsMatch(token, info);
            if(matches) {
                jedisClient.delKey(username+"_number");
            }
            return matches;
    }
}
