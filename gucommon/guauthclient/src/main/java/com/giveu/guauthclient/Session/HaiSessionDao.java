package com.giveu.guauthclient.Session;


import com.giveu.guauthclient.util.SerializableUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.giveu.guauthclient.util.JedisClient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class HaiSessionDao extends CachingSessionDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(HaiSessionDao.class);
    @Autowired
    private JedisClient jedisClient;

    // 局部会话key
    private final static String ZHENG_UPMS_CLIENT_SESSION_ID = "zheng-upms-client-session-id";
    // 单点同一个code所有局部会话key
    private final static String ZHENG_UPMS_CLIENT_SESSION_IDS = "zheng-upms-client-session-ids";

    @Value("${Hai_SERVER_SESSION_ID}")
    private  String server_session_id;

    @Value("${Hai_SERVER_SESSION_IDS}")
    private  String server_session_ids;

    @Value("${Hai_SERVER_CODE}")
    private  String hai_server_code;

    @Value("${Hai_SHIRO_SESSION_ID}")
    private String hai_shiro_id;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        jedisClient.set(hai_shiro_id + "_" + sessionId, SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        LOGGER.debug("doCreate >>>>> sessionId={}", sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = jedisClient.get(hai_shiro_id + "_" + sessionId);
        LOGGER.debug("doReadSession >>>>> sessionId={}", sessionId);
        return SerializableUtil.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        // 更新session的最后一次访问时间
        HaiSession HaiSession = (HaiSession) session;
        HaiSession cacheHaiSession = (HaiSession) doReadSession(session.getId());
        if (null != cacheHaiSession) {
            HaiSession.setStatus(cacheHaiSession.getStatus());
            HaiSession.setAttribute("FORCE_LOGOUT", cacheHaiSession.getAttribute("FORCE_LOGOUT"));
        }
        jedisClient.set(hai_shiro_id + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        // 更新ZHENG_UPMS_SERVER_SESSION_ID、ZHENG_UPMS_SERVER_CODE过期时间 TODO
        LOGGER.debug("doUpdate >>>>> sessionId={}", session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
            // 当前全局会话code
            String code = jedisClient.get(server_session_id + "_" + sessionId);
            // 清除全局会话
            jedisClient.delKey(server_session_id + "_" + sessionId);
            // 清除code校验值
            jedisClient.delKey(hai_server_code + "_" + code);

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
        jedisClient.set(hai_shiro_id + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
    }

}