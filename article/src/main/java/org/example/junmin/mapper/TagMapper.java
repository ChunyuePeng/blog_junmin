package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.Tag;

import java.util.List;

@Mapper
public interface TagMapper {
    Tag selectById(Long id);

    List<Tag> selectList(Tag tag);

    int insert(Tag tag);

    int updateById(Tag tag);

    int deleteById(Long id);
}
