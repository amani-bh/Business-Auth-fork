package com.courzelo_for_business.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BusinessAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusinessAuthApplication.class, args);
	}

}
