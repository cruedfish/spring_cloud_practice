package com.giveu.item.guitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class FeignTestController {
    @Autowired
    public  commonService commonservice;

    @RequestMapping(value = "/feign")
    public  String FeignTest(){
     String a= commonservice.feignTest();
     return  a;
    }
}
