package com.giveu.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableScheduling
@EnableFeignClients
public class ZuulApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(ZuulApplication.class, args);

	}
	@Bean
	public  IpFilter getIpFilter(){
		return new IpFilter();
	}
}
