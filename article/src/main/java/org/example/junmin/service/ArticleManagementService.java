package org.example.junmin.service;

import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.vo.Result;

public interface ArticleManagementService {
    Result addArticle(RequestDTO<Object, ArticleDTO> article);

    Result listArticles(RequestDTO<Object, Object> requestDTO);


    Result getArticleDetail(RequestDTO<GetArticleDetailQuery, Object> requestDTO);
}
