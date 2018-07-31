package com.giveu.guauthclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.giveu.guauthclient","com.giveu.guauth"})
public class GuauthclientApplication {

	public static void main(String[] args) {

		SpringApplication.run(GuauthclientApplication.class, args);
	}
}
