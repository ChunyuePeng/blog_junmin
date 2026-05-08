package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SysUserRoleMapper {
    int insert(@Param("userId") Long userId,
               @Param("roleId") Long roleId);

    int deleteByUserId(Long userId);

    int deleteByRoleId(Long roleId);
}
