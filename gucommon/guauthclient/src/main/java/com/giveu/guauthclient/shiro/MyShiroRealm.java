package com.giveu.guauthclient.shiro;

import com.giveu.guauth.entity.User;
import com.giveu.guauth.mapper.UserMapper;
import com.haistore.redis.MD5Util;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 登录认证
     * <p>
     * 四个参数
     * username：认证的实体信息。object,一般存放用户信息对象。可以通过SecurityUtils.getSubject().getPrincipal()获取。
     * password：数据库中保存的密码
     * realmName：当前realm对象的name，调用父类的getName()方法即可
     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        //获取用户账号
//        String username = token.getPrincipal().toString();
//
//        String password = shiroService.getPasswordByUsername(username);
//        if (password != null) {
//
//            String realmName = getName();
//            return new SimpleAuthenticationInfo(username, password, realmName);
//        }
//        return null;
//    }

    @Override
    protected org.apache.shiro.authc.AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户账号
        String username = token.getPrincipal().toString();

        String password = token.getCredentials().toString();
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
