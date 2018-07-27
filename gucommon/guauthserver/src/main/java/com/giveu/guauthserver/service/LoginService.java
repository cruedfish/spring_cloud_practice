package com.giveu.guauthserver.service;


import com.giveu.guauthclient.Session.HaiSession;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
public interface LoginService {
    public void updateSessionStatus(String sessionID,HaiSession.OnlineStatus status);
}
