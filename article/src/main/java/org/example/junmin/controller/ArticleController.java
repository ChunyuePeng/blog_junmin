package org.example.junmin.controller;

import jakarta.annotation.Resource;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.service.ArticleManagementService;
import org.example.junmin.vo.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article-management/")
public class ArticleController {

    @Resource
    private ArticleManagementService articleManagementService;
    @PostMapping("add-article")
    public Result addArticle(@RequestBody RequestDTO<Object,ArticleDTO> article) {
        return articleManagementService.addArticle(article);
    }

    @PostMapping("list-articles")
    public Result listArticles(@RequestBody RequestDTO<Object,Object> requestDTO) {
        return articleManagementService.listArticles(requestDTO);
    }
}
