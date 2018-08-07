package com.giveu.guauthclient.config;

import com.giveu.guauthclient.Session.HaiSessionDao;
import com.giveu.guauthclient.Session.HaiSessionFactory;
import com.giveu.guauthclient.filter.UserAuthFilter;
import com.giveu.guauthclient.shiro.MyShiroRealm;
import com.giveu.guauthclient.util.JedisClient;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * <P></P>
 *
 * @author zhaodong
 * @version v1.0
 * @email zhaodongxx@outlook.com
 * @since 2018/3/30 22:41
 */
@Configuration
public class ShiroConfig {
    private final Logger logger = Logger.getLogger(ShiroConfig.class);

//    @Value("${sso.server.uri}")
    private String loginUri = "http://localhost:7009/sso/login";
    /**
     * 凭证匹配器
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        /**
         * hash算法:这里使用MD5算法;
         */
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        /**
         * 散列的次数，比如散列两次，相当于 md5(md5(""));
         */
        hashedCredentialsMatcher.setHashIterations(1);

        return hashedCredentialsMatcher;
    }

    /**
     * 自定义的Realm
     */
    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // //注入ehcache缓存管理器;
        securityManager.setCacheManager(ehCacheManager());
        // //注入session管理器;
        securityManager.setSessionManager(sessionManager());
        //注入Cookie记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }
    public UserAuthFilter getUserAuthFilter(){
        UserAuthFilter userAuthFilter = new UserAuthFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        userAuthFilter.setCacheManager(ehCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        userAuthFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        userAuthFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        userAuthFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        userAuthFilter.setKickoutUrl("/toLogin?kickout=1");
            return userAuthFilter;
    }
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        HashMap<String,Filter> hashMap=new HashMap<String,Filter>();
        hashMap.put("authc",getUserAuthFilter());
        shiroFilterFactoryBean.setFilters(hashMap);
        Map<String,String> filterMap = new HashMap<String,String>();
        filterMap.put("/*/login","anon");
        filterMap.put("/SSO/registry","anon");
        filterMap.put("/SSO/*","authc");
        filterMap.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }
    @Bean
    public EhCacheManager ehCacheManager(){
        logger.debug(
                "=====shiro整合ehcache缓存：ShiroConfiguration.getEhCacheManager()");
        EhCacheManager ehcache = new EhCacheManager();
        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
        if(cacheManager == null){
            try {
                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:config/ehcache.xml"));
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
        ehcache.setCacheManager(cacheManager);
        return ehcache;
    }
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(haiSessionDao());
        sessionManager.setSessionFactory(haiSessionFactory());
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }
    @Bean
    public HaiSessionDao haiSessionDao(){
        return new HaiSessionDao();
    }
    /**
     *
     * @描述：自定义cookie中session名称等配置
     * @创建人：wyait
     * @创建时间：2018年5月8日 下午1:26:23
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        //DefaultSecurityManager
        SimpleCookie simpleCookie = new SimpleCookie();
        //sessionManager.setCacheManager(ehCacheManager());
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("SHRIOSESSIONID");
        //单位秒
        simpleCookie.setMaxAge(86400);
        return simpleCookie;
    }
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        logger.debug("配置cookie记住我管理器！");
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }
    /**
     * 设置记住我cookie过期时间
     * @return
     */
    @Bean
    public SimpleCookie remeberMeCookie(){
        logger.debug("记住我，设置cookie过期时间！");
        //cookie名称;对应前端的checkbox的name = rememberMe
        SimpleCookie scookie=new SimpleCookie("rememberMe");
        //记住我cookie生效时间30天 ,单位秒  [10天]
        scookie.setMaxAge(864000);
        return scookie;
    }
    @Bean
    public JedisClient jedisClient(){
        return new JedisClient();
    }

    @Bean
    public HaiSessionFactory haiSessionFactory(){
       return new HaiSessionFactory();
    }
}
