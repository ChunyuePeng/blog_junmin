package org.example.junmin.service;

import org.example.junmin.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user-management/get-user-by-username")
    UserDTO getUserById(@RequestParam("username") String username);
}
