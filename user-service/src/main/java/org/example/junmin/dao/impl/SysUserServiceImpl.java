package org.example.junmin.dao.impl;

import jakarta.annotation.Resource;
import org.example.junmin.dao.service.SysUserService;
import org.example.junmin.entity.SysUser;
import org.example.junmin.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public int addUser(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    @Override
    public SysUser getByUsername(String userName) {
        return sysUserMapper.selectByUsername(userName);
    }
}
