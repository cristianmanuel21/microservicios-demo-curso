package com.springboot.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringbootServicioGatewaySeverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioGatewaySeverApplication.class, args);
	}

}
