package com.giveu.gucart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class TestController {
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public  String test(){
        return  "1";
    }
}
