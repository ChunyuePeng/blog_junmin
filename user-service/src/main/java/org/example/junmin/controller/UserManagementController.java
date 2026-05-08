package org.example.junmin.controller;

import jakarta.annotation.Resource;
import org.example.junmin.dto.UserDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.service.UserManagementService;
import org.example.junmin.vo.Result;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user-management/")
@RestController
public class UserManagementController {
    @Resource
    private UserManagementService userManagementService;

    @PostMapping("add-user")
    public Result addUser(@RequestBody RequestDTO<Object,UserDTO> requestDTO) {
        return userManagementService.addUser(requestDTO);
    }

    @GetMapping("get-user-by-username")
    public UserDTO  getUserByUsername(@RequestParam("username") String userName) {
        return userManagementService.getUserByUsername(userName);
    }

    @GetMapping("get-user-id-by-username")
    public Long  getUserIdByUsername(@RequestParam("username") String userName) {
        return userManagementService.getUserIdByUsername(userName);
    }

}
