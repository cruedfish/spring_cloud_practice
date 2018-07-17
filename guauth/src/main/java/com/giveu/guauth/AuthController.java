package com.giveu.guauth;

import com.giveu.guauth.entity.User;
import com.giveu.guauth.mapper.UserMapper;
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
@Controller
@RequestMapping(value = "authorization")
public class AuthController {
    @Autowired
    private UserMapper userMapper;
    Logger logger = Logger.getLogger(AuthController.class);

    @RequestMapping(value = "/getToken",consumes = MediaType.APPLICATION_JSON_VALUE ,method = RequestMethod.POST)
    @ResponseBody
    public String getToken(@RequestBody Map<String,String> paramMap){
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");
        User user = userMapper.selectById(1);
        logger.info(user.toString()+"");
        return null;
    }
}
