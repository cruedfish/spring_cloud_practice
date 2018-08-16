package com.giveu.guauthclient.Session;


import com.giveu.guauthclient.util.JedisClient;
import com.giveu.guauthclient.util.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.util.SerializationUtils;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class HaiSessionDao extends CachingSessionDAO{

    private Logger logger = Logger.getLogger(HaiSessionDao.class);

    // 局部会话key
    private final static String ZHENG_UPMS_CLIENT_SESSION_ID = "zheng-upms-client-session-id";
    // 单点同一个code所有局部会话key
    private final static String ZHENG_UPMS_CLIENT_SESSION_IDS = "zheng-upms-client-session-ids";

    @Autowired
    private JedisClient jedisClient;

    @Value("${Hai_SHIRO_SESSION_ID}")
    private String hai_shiro_id;

    @Value("${SESSION_ID}")
    private  String session_id;

    @Value("${SSO_Id}")
    private String sso_id;


    @Override
    public Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        jedisClient.set(hai_shiro_id + "_" + sessionId, SerializationUtils.serialize(session), (int) session.getTimeout() / 1000);
        logger.debug("doCreate >>>>> sessionId={}"+session_id);
        return sessionId;
    }

    @Override
    public HaiSession doReadSession(Serializable sessionId) {
        byte[] sessionByte = jedisClient.getByte(hai_shiro_id + "_" + sessionId);
        Session session = (Session) SerializationUtils.deserialize(sessionByte);
        HaiSession haiSession = (HaiSession) session;
        logger.debug("doReadSession >>>>> sessionId={}"+sessionId);
        return haiSession ;
    }

    @Override
    public void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        // 更新session的最后一次访问时间
        HaiSession haiSession = (HaiSession) session;
        HaiSession cacheHaiSession = (HaiSession) doReadSession(session.getId());
        jedisClient.set(hai_shiro_id + "_" + session.getId(), SerializationUtils.serialize(session), (int) session.getTimeout() / 1000);
        // 更新ZHENG_UPMS_SERVER_SESSION_ID、ZHENG_UPMS_SERVER_CODE过期时间 TODO
        logger.debug("doUpdate >>>>> sessionId={}"+ session.getId());
    }

    @Override
    public void doDelete(Session session) {
        String sessionId = session.getId().toString();
            // 清除全局会话
        jedisClient.delKey(session_id + "_" + sessionId);

    }

    /**
     * 获取会话列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public Map getActiveSessions(int offset, int limit) {
        Map sessions = new HashMap();
        return null;
    }

    /**
     * 强制退出
     *
     * @param ids
     * @return
     */
    public int forceout(String ids) {
        String[] sessionIds = ids.split(",");

        return sessionIds.length;
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, HaiSession.OnlineStatus onlineStatus) {
        HaiSession session = (HaiSession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        jedisClient.set(hai_shiro_id + "_" + session.getId(), SerializationUtils.serialize(session), (int) session.getTimeout() / 1000);
    }
  
}