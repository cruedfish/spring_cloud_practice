package com.giveu.guauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GuauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuauthApplication.class, args);
	}
}
