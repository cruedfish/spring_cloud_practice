package com.giveu.zuul;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@FeignClient(name = "auth" ,fallback = AuthRemoteClientHystrix.class)
public interface AuthRemoteClient {
    @RequestMapping(value = "/getToken",method = RequestMethod.POST)
    Map<String,Object> getToken();
}
