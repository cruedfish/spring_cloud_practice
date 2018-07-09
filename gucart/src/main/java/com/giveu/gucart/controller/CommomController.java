package com.giveu.gucart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class CommomController {
    @RequestMapping("/cartPage")
    public String  redirectIndex(){
        return "cart";
    }
    @RequestMapping("/feignTest")
    public String  FeignTest(){
        return "hello world123";
    }
}
