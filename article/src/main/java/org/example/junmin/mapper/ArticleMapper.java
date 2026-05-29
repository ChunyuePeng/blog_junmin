package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.junmin.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {
    Article selectById(Long id);

    List<Article> selectByUserId(Long userId);

    List<Article> selectList(Article article);

    int insert(Article article);

    int updateById(Article article);

    int deleteById(Long id);
    @Update("UPDATE articles SET view_count = #{views} WHERE id = #{id}")
    int updateViewCount(@Param(value = "id") Long id,@Param(value = "views") Integer views);

}
