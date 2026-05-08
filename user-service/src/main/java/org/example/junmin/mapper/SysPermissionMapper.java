package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.SysPermission;

import java.util.List;

@Mapper
public interface SysPermissionMapper {
    SysPermission selectById(Long id);

    List<SysPermission> selectAll();

    int insert(SysPermission permission);

    int update(SysPermission permission);

    int deleteById(Long id);
}
