package com.giveu.gustore1;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gustore1Application {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Gustore1Application.class);
		SpringApplication.run(Gustore1Application.class, args);
	}
}
