package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
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

}
