package org.example.junmin.dao.service;

import org.example.junmin.entity.Article;

import java.util.List;

public interface ArticleService {
    void save(Article article);

    List<Article> getAll();

    Article getById(Long id);

    void updateViewCount(Long articleId, Long views);
}
