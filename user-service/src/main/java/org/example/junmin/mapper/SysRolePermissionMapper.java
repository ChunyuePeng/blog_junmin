package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRolePermissionMapper {
    int insert(@Param("roleId") Long roleId,
               @Param("permissionId") Long permissionId);

    int deleteByRoleId(Long roleId);
}
