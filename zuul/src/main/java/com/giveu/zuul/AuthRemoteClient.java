package com.giveu.zuul;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@FeignClient(name = "auth" ,fallback = AuthRemoteClientHystrix.class)
@RestController
@RequestMapping(value = "authorization")
public interface AuthRemoteClient {
   @RequestMapping(value = "/getToken")
   public Map<String,Object> getToken();
}
