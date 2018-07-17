/*
 * *****************************************************************************
 *  达飞金融，机密
 *  __________________
 * Copyright@2015-2016 DAFY CREDIT ALL Rights Reserved 达飞金融旗下品牌即有分期 版权所有
 * 备案号：粤ICP备17014188号
 *
 * 注意：
 * 	此处包含的所有信息均为深圳前海达飞金融服务有限公司的财产。知识和技术理念包含在内为深圳前
 * 	海达飞金融服务有限公司所有，可能受中国和国际专利，以及商业秘密或版权法保护。严格禁止传播
 * 	此信息或复制此材料，除非事先获得来自深圳前海达飞金融服务有限公司的书面许可。
 */

package com.giveu.guauth;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <br>
 * Druid数据监控</br>
 *
 * @author 100196
 * @email dengjiaxing@dafycredit.com
 * @date 2017年12月10日 下午1:47:31
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class DruidDataSourceConfig {

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean DruidStatViewServle2() {
		// org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// 添加初始化参数：initParams
		// 白名单：
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1,10.10.75.30");
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
		// 登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * 注册一个：filterRegistrationBean
	 *
	 * @return
	 */
	@Bean
	public FilterRegistrationBean druidStatFilter2() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

}
