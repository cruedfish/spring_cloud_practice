package com.giveu.guoauth2.service;

import com.giveu.gucommon.entity.sysUser;
import com.giveu.gucommon.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DomainUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String lowcaseUsername = username.toLowerCase();
        sysUser user = userMapper.selectById(username);
        try {
            if (user != null) {
                return new User(user.getName(), user.getPassword(), null);
            }
        }catch (Exception e){
               throw  new UsernameNotFoundException("用户"+username+"不存在");
        }
        return null;
    }
}
