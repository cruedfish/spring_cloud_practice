package com.giveu.guauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.giveu.guauth.mapper")
public class GuauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuauthApplication.class, args);
	}
}
