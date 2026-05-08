package org.example.junmin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.junmin.entity.ArticleTag;

import java.util.List;

@Mapper
public interface ArticleTagMapper {
    List<Long> selectTagIdsByArticleId(Long articleId);

    int batchInsert(List<ArticleTag> list);

    int deleteByArticleId(Long articleId);
}
