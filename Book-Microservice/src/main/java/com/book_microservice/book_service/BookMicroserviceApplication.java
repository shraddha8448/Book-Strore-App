package com.book_microservice.book_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookMicroserviceApplication.class, args);
	}

}
