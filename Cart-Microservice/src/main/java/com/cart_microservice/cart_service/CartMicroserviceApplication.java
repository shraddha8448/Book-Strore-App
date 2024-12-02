package com.cart_microservice.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CartMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartMicroserviceApplication.class, args);
	}

}