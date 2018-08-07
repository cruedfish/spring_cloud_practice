package com.giveu.gucart.controller;

import com.alibaba.fastjson.JSONObject;
import com.giveu.gucart.HaiResult;
import com.haistore.redis.JedisClientSingle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class CommomController {

    private JedisClientSingle jedisClientSingle = new JedisClientSingle();

    @Value("${goodName1}")
    private String goodName;

    @Value("${yinhai.test}")
    private String yinhai;

    @ApiOperation(value="创建条目2")
    @RequestMapping(value = "/cartPage", method= RequestMethod.GET)
    public String  redirectIndex(){
        jedisClientSingle.set("token","test1",60*60*2);
        return yinhai;
    }

    @ApiOperation(value="创建条目")
    @RequestMapping(value = "/aaa" ,method=RequestMethod.GET ,produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> FeignTest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodName",goodName);
        return HaiResult.buildsucc("0","请求成功",jsonObject);
    }
}
