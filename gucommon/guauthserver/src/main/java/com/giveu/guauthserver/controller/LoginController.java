package com.giveu.guauthserver.controller;

import com.giveu.guauthclient.Session.HaiSession;
import com.giveu.guauthserver.JedisClient;
import com.giveu.guauthserver.service.LoginService;
import com.haistore.redis.HaiResult;
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
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: YinHai
 * @Descripation:
 * @Date: Created in ${time} ${Date}
 */
@RestController
@RequestMapping(value = "SSO")
@Api(value = "单点登录管理" , description = "单点登录管理")
public class LoginController {

    @Value("${Hai_SERVER_SESSION_ID}")
    private  String server_session_id;

    @Value("${Hai_SERVER_SESSION_IDS}")
    private  String server_session_ids;

    @Value("${Hai_SERVER_CODE}")
    private  String hai_server_code;


    @Autowired
    LoginService loginService;

    @Autowired
    private JedisClient jedisClient;

    private Logger logger = Logger.getLogger(LoginController.class);

    @ApiOperation(value = "跳转到登录页面")
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳
        String code = jedisClient.get(serverSessionId + "_" + serverSessionId);
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
        return "/sso/login.jsp";
    }


    @RequestMapping(value = "/login" ,consumes = MediaType.APPLICATION_JSON_VALUE ,method = RequestMethod.POST)
    public Map<String,Object>  login(@RequestBody Map<String,String> paramMap){
        String username = paramMap.get("username");
        String password = paramMap.get("password");
        String remeberOr = paramMap.get("remeberOr");
        if (StringUtils.isBlank(username)) {
            return HaiResult.buildFail("-1","账号不能为空",null);
        }
        if (StringUtils.isBlank(password)) {
            return HaiResult.buildFail("-1","密码不能为空",null);
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionID = session.getId().toString();
        String hashCode = jedisClient.get(server_session_id+"_"+server_session_id);
        if(StringUtils.isBlank(hashCode)){
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);
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
            }
            loginService.updateSessionStatus(sessionID, HaiSession.OnlineStatus.on_line);
            // 全局会话sessionId列表，供会话管理
            jedisClient.lpush(server_session_ids,sessionID.toString());
            // 默认验证帐号密码正确，创建code
            String code = UUID.randomUUID().toString();
            // 全局会话的code
            jedisClient.set(server_session_id + "_" + sessionID, code, (int) subject.getSession().getTimeout() / 1000);
            // code校验值
            jedisClient.set(hai_server_code + "_" + code, code, (int) subject.getSession().getTimeout() / 1000);
        }
        String backurl = paramMap.get("backurl");
        if(backurl == null || "0".equals(backurl.length())){
            return HaiResult.buildFail("-1","回调url为空",null);
        }
        return HaiResult.buildsucc("0","身份验证成功",backurl);
    }

}
