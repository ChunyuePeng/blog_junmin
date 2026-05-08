package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category selectById(Long id);

    List<Category> selectList(Category category);

    int insert(Category category);

    int updateById(Category category);

    int deleteById(Long id);
}
