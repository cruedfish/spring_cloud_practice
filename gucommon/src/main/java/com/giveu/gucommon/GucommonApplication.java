package com.giveu.gucommon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootApplication
public class GucommonApplication {
	public static void main(String[] args) {
		SpringApplication.run(GucommonApplication.class, args);
	}

}
