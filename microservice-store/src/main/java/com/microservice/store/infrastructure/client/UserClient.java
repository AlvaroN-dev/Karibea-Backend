package com.microservice.store.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-user", path = "/users")
public interface UserClient {

    @GetMapping("/{id}")
    Object getUser(@PathVariable("id") String id);
}
