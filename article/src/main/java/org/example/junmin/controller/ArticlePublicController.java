package org.example.junmin.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.junmin.comm.query.BlogSearchQuery;
import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.service.ArticleManagementService;
import org.example.junmin.vo.Result;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @GetMapping("get-tag-list")
    public Result  getTagList() {
        return articleManagementService.getTagList();
    }

    @GetMapping("get-image")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam("fileName") String fileName, HttpServletResponse response) throws FileNotFoundException {
        return articleManagementService.getImage(fileName,response);
    }


    @PostMapping("/search")
    public Result search(@RequestBody RequestDTO<BlogSearchQuery,Object> requestDTO)
            throws IOException {

        return articleManagementService.search(requestDTO);
    }

    @GetMapping("/top10")
    public Result top10() {
        return articleManagementService.top10();
    }
}
