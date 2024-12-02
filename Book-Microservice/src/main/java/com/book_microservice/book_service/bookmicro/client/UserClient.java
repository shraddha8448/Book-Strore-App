package com.book_microservice.book_service.bookmicro.client;

import com.book_microservice.book_service.bookmicro.external.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "USER-MICROSERVICE")
public interface UserClient {
    @GetMapping("user/getUser")
    User getUser(@RequestParam("authHeader") String authHeader);

}