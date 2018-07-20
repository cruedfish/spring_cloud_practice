package com.giveu.zuul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haistore.redis.HaiResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class TokenScheduledTask {

    @Autowired
    private AuthRemoteClient authRemoteClient;

    Logger logger = Logger.getLogger(TokenScheduledTask.class);

    private final static Semaphore semaphore = new Semaphore(5);
    public final static long ONE_Minute = 60 * 1000 * 60 * 2;


    @Scheduled(fixedDelay = ONE_Minute)
    public Map<String,Object> reloadApiToken(){
        try {
            Map<String,Object> resToken = authRemoteClient.getToken();
            if("success".equals(resToken.get("status"))){
                if(resToken.get("data") == null){
                    semaphore.acquire();
                    reloadApiToken();
                }else{
                    System.setProperty("hai.store.token", JSONObject.toJSON(resToken.get("data") )+"" );
                    semaphore.release(5);
                }
            }
        }catch (Exception e){
            return HaiResult.buildFail("-1","获取token异常",null);
        }
        return null;
    }
}
