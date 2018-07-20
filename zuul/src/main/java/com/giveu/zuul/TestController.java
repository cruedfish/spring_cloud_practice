package com.giveu.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class TestController {
    @Autowired
    private AuthRemoteClient authRemoteClient;
    @RequestMapping(value = "/test" ,method = RequestMethod.POST)
    public void test(){
        Map<String,Object> map = authRemoteClient.getToken();
        int a = 1;
    }
}
