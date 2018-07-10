package com.giveu.gucart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class CommomController {
    @RequestMapping(value = "/cartPage", method= RequestMethod.GET)
    public String  redirectIndex(){
        return "cart";
    }
    @RequestMapping(value = "/aaa")
    public String  FeignTest(){
        return "hello world123";
    }
}
