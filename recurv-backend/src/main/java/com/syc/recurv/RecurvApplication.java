package com.syc.recurv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAsync
@EnableCaching(proxyTargetClass = true)
@EnableScheduling
public class RecurvApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecurvApplication.class, args);
	}

}
