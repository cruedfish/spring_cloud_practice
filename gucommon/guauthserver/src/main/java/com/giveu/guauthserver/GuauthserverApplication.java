package com.giveu.guauthserver;

import com.giveu.guauthclient.config.ShiroConfig;
import com.giveu.guauthclient.filter.UserAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
@ComponentScan(basePackages = {"com.giveu.guauthclient","com.giveu.guauth","com.giveu.guauthserver"})
public class GuauthserverApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuauthserverApplication.class, args);
	}
}
