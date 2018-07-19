package com.giveu.zuul;

import com.haistore.redis.HaiResult;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class AuthRemoteClientHystrix implements   AuthRemoteClient{
    @Override
    public Map<String, Object> getToken() {
        return HaiResult.buildFail("-1","网关转发异常",null);
    }
}
