package com.giveu.guconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class GuconfigApplication {
	public static void main(String[] args) {
		SpringApplication.run(GuconfigApplication.class, args);
	}
}
