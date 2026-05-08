package org.example.junmin.controller;

import jakarta.annotation.Resource;
import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.service.ArticleManagementService;
import org.example.junmin.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article-public/")
public class ArticlePublicController {

    @Resource
    private ArticleManagementService articleManagementService;

    @PostMapping("list-articles")
    public Result listArticles(@RequestBody RequestDTO<Object,Object> requestDTO) {
        return articleManagementService.listArticles(requestDTO);
    }

    @PostMapping("get-article-detail")
    public Result getArticleDetail(@RequestBody RequestDTO<GetArticleDetailQuery,Object> requestDTO) {
        return articleManagementService.getArticleDetail(requestDTO);
    }
}
