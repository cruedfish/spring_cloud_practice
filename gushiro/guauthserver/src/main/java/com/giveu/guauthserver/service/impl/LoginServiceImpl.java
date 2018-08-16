package com.giveu.guauthserver.service.impl;

import com.giveu.guauthclient.Session.HaiSession;
import com.giveu.guauthclient.Session.HaiSessionDao;
import com.giveu.guauthclient.util.JedisClient;
import com.giveu.guauthserver.service.BaseService;
import com.giveu.guauthserver.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Service
public class LoginServiceImpl extends BaseService implements LoginService {
    @Autowired
    JedisClient jedisClient;
    @Autowired
    HaiSessionDao haiSessionDao;
    @Override
    public void updateSessionStatus(String sessionID,HaiSession.OnlineStatus status) {
        haiSessionDao.updateStatus(sessionID,status);
    }
}
