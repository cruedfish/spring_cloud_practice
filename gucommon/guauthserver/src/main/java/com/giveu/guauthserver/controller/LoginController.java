package com.giveu.guauthserver.controller;

import com.giveu.guauth.entity.User;
import com.giveu.guauth.mapper.UserMapper;
import com.giveu.guauthclient.Session.HaiSession;
import com.giveu.guauthclient.Session.HaiSessionDao;
import com.giveu.guauthclient.util.JedisClient;
import com.giveu.guauthserver.service.LoginService;
import com.haistore.redis.HaiResult;
import com.haistore.redis.MD5Util;
import com.haistore.redis.ProtostuffSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@Controller
@RequestMapping(value = "SSO")
@Api(value = "单点登录管理" , description = "单点登录管理")
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HaiSessionDao haiSessionDao;

    @Value("${SESSION_ID}")
    private  String session_id;

    @Value("${SSO_Id}")
    private String sso_id;

    private Logger logger = Logger.getLogger(LoginController.class);

    private int MaxSession = 1;

    private Boolean KickAfter = true;

    private ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();

    @ApiOperation(value = "跳转到登录页面")
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sid = session.getId().toString();
        // 根据sessionId判断是否已登录，如果已登录，则回跳
        String code = jedisClient.get(session_id + "_" + sid);
        // code校验值
        if (StringUtils.isNotBlank(code)) {
            // 回跳
            String backurl = request.getParameter("backurl");
            String username = (String) subject.getPrincipal();
            if (StringUtils.isBlank(backurl)) {
                backurl = "/";
            } else {
                if (backurl.contains("?")) {
                    backurl += "&hai_code=" + code + "&hai_username=" + username;
                } else {
                    backurl += "?hai_code=" + code + "&hai_username=" + username;
                }
            }
            logger.info("认证中心帐号通过，带code回跳：{}"+ backurl);
            return "redirect:" + backurl;
        }
        return "login";
    }

    @RequestMapping(value = "/test" ,method = RequestMethod.GET)
    public Map<String,Object>  test(){
        String a="hehehehh";
        return HaiResult.buildsucc("0",a,null);
    }

    @RequestMapping(value = "/login" ,consumes = MediaType.APPLICATION_JSON_VALUE ,method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>  login(@RequestBody Map<String,String> paramMap){
        String username = paramMap.get("username");
        String password = paramMap.get("password");
        String remeberOr = paramMap.get("remeberOr");
        String backurl = paramMap.get("backurl");
        BigInteger Id = BigInteger.valueOf(Long.valueOf(paramMap.get("Id")));
        if (StringUtils.isBlank(username)) {
            return HaiResult.buildFail("-1","账号不能为空",null);
        }
        if (StringUtils.isBlank(password)) {
            return HaiResult.buildFail("-1","密码不能为空",null);
        }

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionID = session.getId().toString();
        String hashCode = jedisClient.get(session_id+"_"+sessionID);

        List<String> sessionList = jedisClient.lrang(sso_id+""+Id,0,-1);

        if(StringUtils.isBlank(hashCode)){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(Id.toString(),password);
            try {
                if (BooleanUtils.toBoolean(remeberOr)) {
                    usernamePasswordToken.setRememberMe(true);
                } else {
                    usernamePasswordToken.setRememberMe(false);
                }
                subject.login(usernamePasswordToken);
            }catch (UnknownAccountException e) {
                return HaiResult.buildFail("-1","账号不存在",null);
            } catch (IncorrectCredentialsException e) {
                return HaiResult.buildFail("-1","密码错误",null);
            } catch (LockedAccountException e) {
                return HaiResult.buildFail("-1","账号已锁定",null);
            } catch (Exception e) {
                return HaiResult.buildFail("-1","账号已锁定",null);
            }
            if(!sessionList.contains(sessionID+"") && session.getAttribute("kickout") == null){
                loginService.updateSessionStatus(sessionID, HaiSession.OnlineStatus.on_line);

                // 默认验证帐号密码正确，创建code
                String code = UUID.randomUUID().toString();
                // 全局会话的code
                jedisClient.set( session_id+ "_" + sessionID, code, (int) subject.getSession().getTimeout() / 1000);

                jedisClient.lpush(sso_id+"_"+Id,sessionID);

                sessionList.add(sessionID);

                jedisClient.expire(sso_id+"_"+Id,(int) subject.getSession().getTimeout() / 1000);
            }
            logger.debug("==session时间设置：" + String.valueOf(session.getTimeout())
                    + "===========");

            while (sessionList.size() > MaxSession){
                logger.info("现存的SessionId队列长度"+sessionList.size());
                String kickId = "";
                if(KickAfter == true){
                    kickId = jedisClient.lpop(sso_id+"_"+Id);
                }else{
                    kickId = jedisClient.rpop(sso_id+"_"+Id);
                }
                Session kickSession = haiSessionDao.readSession(protostuffSerializer.serialize(kickId));
                if (kickSession != null) {
                    // 设置会话的kickout属性表示踢出了
                    kickSession.setAttribute("kickout", true);
                }
            }
            if ((Boolean) session.getAttribute("kickout") != null
                    && (Boolean) session.getAttribute("kickout") == true) {
                subject.logout();
                return HaiResult.buildFail("-1","身份验证失败",backurl);
            }

        }
        return HaiResult.buildsucc("0","身份验证成功",backurl);
    }

    @RequestMapping(value = "/registry" ,consumes = MediaType.APPLICATION_JSON_VALUE ,method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>  registry(@RequestBody Map<String,String> paramMap) {
        BigInteger Id = BigInteger.valueOf(Long.valueOf(paramMap.get("Id")));
        String username = paramMap.get("username");
        String password = paramMap.get("password");
        String salt = UUID.randomUUID().toString();
        String md5pass = "";
        try {
             md5pass = MD5Util.md5LowerCase(password,salt);
        }catch (Exception e){
            return HaiResult.buildFail("-1","系统加密异常",null);
        }
        User user = new User();
        user.setAge("22");
        user.setId(Id);
        user.setPassword(md5pass);
        user.setName(username);
        user.setSex("女");
        user.setSalt(salt);
        userMapper.insert(user);
        return HaiResult.buildsucc("0","插入成功",null);
    }

}
