package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.SysUser;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUser selectById(Long id);

    SysUser selectByUsername(String username);

    List<SysUser> selectAll();

    int insert(SysUser user);

    int update(SysUser user);

    int deleteById(Long id);
}
