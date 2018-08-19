package com.giveu.guoauth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.giveu.gucommon.mapper")
public class Guoauth2Application {

	public static void main(String[] args) {
		SpringApplication.run(Guoauth2Application.class, args);
	}
}
