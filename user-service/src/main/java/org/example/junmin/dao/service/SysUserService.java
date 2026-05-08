package org.example.junmin.dao.service;

import org.example.junmin.entity.SysUser;

public interface SysUserService {
    int addUser(SysUser sysUser);

    SysUser getByUsername(String userName);
}
