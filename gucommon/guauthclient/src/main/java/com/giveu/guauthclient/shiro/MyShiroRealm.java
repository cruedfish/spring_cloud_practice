package com.giveu.guauthclient.shiro;

import com.giveu.guauth.entity.User;
import com.giveu.guauth.mapper.UserMapper;
import com.haistore.redis.MD5Util;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       return null;

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        User user = userMapper.selectById(username);
        if(null == user){
            throw  new UnknownAccountException();
        }
        if(user.getPassword() != MD5Util.md5(password+""+user.getSalt())){
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
