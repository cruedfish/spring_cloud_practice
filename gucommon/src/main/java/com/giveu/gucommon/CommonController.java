package com.giveu.gucommon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class CommonController {
    @Value("${spring.redis.host}")
    private String host;

    @Autowired
    JedisClient jedisClient;
    @RequestMapping(value = "test")
    public String getTest1(){
//        String a = jedisClient.get("test1");
        return  "3";
    }

}
