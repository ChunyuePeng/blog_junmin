package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    SysRole selectById(Long id);

    List<SysRole> selectAll();

    int insert(SysRole role);

    int update(SysRole role);

    int deleteById(Long id);
}
