package com.giveu.guauth;

import com.alibaba.fastjson.JSONObject;
import com.giveu.guauth.entity.TokenSubject;
import com.giveu.guauth.entity.User;
import com.giveu.guauth.mapper.TokenSubjectMapper;
import com.giveu.guauth.mapper.UserMapper;
import com.haistore.redis.HaiResult;
import com.haistore.redis.JWTUtils;
import com.haistore.redis.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
public class AuthController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenSubjectMapper tokenSubjectMapper;

    Logger logger = Logger.getLogger(AuthController.class);

    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
        @Override
        public Integer initialValue(){
            return 0;
        }
    };

    @SuppressWarnings(value = "unchecked")
    @RequestMapping(value = "/getToken" ,method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getToken(){
        int sid = threadLocal.get();
        try {
            TokenSubject tokenSubject = tokenSubjectMapper.selectById(++sid);
            String subject = MD5Util.md5LowerCase(tokenSubject.getSubject(),tokenSubject.getSalt());
            JWTUtils instance = JWTUtils.getInstance();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",instance.getToken(subject+"",60*20));
            return HaiResult.buildsucc("0","获取token成功",jsonObject);
        }catch (Exception e){
           return HaiResult.buildFail("-1","获取token失败",null);
        }

    }
}
