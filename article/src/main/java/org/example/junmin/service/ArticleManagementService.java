package org.example.junmin.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.junmin.comm.query.BlogSearchQuery;
import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.vo.Result;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArticleManagementService {
    Result addArticle(RequestDTO<Object, ArticleDTO> article);

    Result listArticles(RequestDTO<Object, Object> requestDTO);


    Result getArticleDetail(RequestDTO<GetArticleDetailQuery, Object> requestDTO);

    Result getTagList();

    Result uploadImage(MultipartFile multipartFile);

    ResponseEntity<InputStreamResource> getImage(String fileName, HttpServletResponse response) throws FileNotFoundException;

    Result search(RequestDTO<BlogSearchQuery, Object> keyword) throws IOException;

    Result top10();

}
