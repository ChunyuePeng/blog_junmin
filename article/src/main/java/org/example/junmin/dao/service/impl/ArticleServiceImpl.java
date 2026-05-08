package org.example.junmin.dao.service.impl;

import jakarta.annotation.Resource;
import org.example.junmin.dao.service.ArticleService;
import org.example.junmin.entity.Article;
import org.example.junmin.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void save(Article article) {
        articleMapper.insert(article);
    }

    @Override
    public List<Article> getAll() {
        return articleMapper.selectList(null);
    }

    @Override
    public Article getById(Long id) {
        return articleMapper.selectById(id);
    }
}
