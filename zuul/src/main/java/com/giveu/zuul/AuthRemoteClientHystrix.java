package com.giveu.zuul;

import com.haistore.redis.HaiResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Component
public class AuthRemoteClientHystrix implements AuthRemoteClient{

    @Override
    public Map<String, Object> getToken() {
        return HaiResult.buildFail("-1","获取token失败",null);
    }
}
