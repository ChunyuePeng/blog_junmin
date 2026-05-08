package org.example.junmin.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.example.junmin.dto.UserDTO;
import org.example.junmin.dao.service.SysUserService;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.entity.SysUser;
import org.example.junmin.service.UserManagementService;
import org.example.junmin.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    @Resource
    private SysUserService sysUserService;
    @Override
    public Result addUser(RequestDTO<Object, UserDTO> requestDTO) {
        UserDTO d = requestDTO.getD();
        if (d == null|| StringUtils.isEmpty(d.getPassword())||StringUtils.isEmpty(d.getUserName())) {
            return Result.fail("The username and password cannot be null");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUsername(d.getUserName());
        sysUser.setPassword(d.getPassword());
        sysUserService.addUser(sysUser);
        return Result.success();
    }

    @Override
    public UserDTO getUserByUsername(String userName) {
        SysUser byUsername = sysUserService.getByUsername(userName);
        if (byUsername == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUserName(byUsername.getUsername());
        dto.setPassword(byUsername.getPassword());
        dto.setUserId(byUsername.getId());
        return dto;
    }

    @Override
    public Long getUserIdByUsername(String userName) {
        SysUser byUsername = sysUserService.getByUsername(userName);
        if (byUsername == null) {
            return null;
        }
        return byUsername.getId();
    }
}
