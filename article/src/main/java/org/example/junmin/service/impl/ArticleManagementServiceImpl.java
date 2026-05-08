package org.example.junmin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.comm.vo.ArticleDetailVO;
import org.example.junmin.config.UserContext;
import org.example.junmin.dao.service.ArticleService;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.Page;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.entity.Article;
import org.example.junmin.service.ArticleManagementService;
import org.example.junmin.util.DateTimeUtils;
import org.example.junmin.vo.PageResult;
import org.example.junmin.vo.Result;
import org.example.junmin.vo.article.ArticleVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleManagementServiceImpl implements ArticleManagementService {
    @Resource
    private ArticleService articleService;
    @Override
    public Result addArticle(RequestDTO<Object, ArticleDTO> requestDTO) {
        Long userId = UserContext.getUser();
        if (userId == null) {
            return Result.fail(401,"Please Login");
        }
        ArticleDTO d = requestDTO.getD();
        if (d == null|| StringUtils.isEmpty(d.getTitle())||StringUtils.isEmpty(d.getContent())) {
            return Result.fail("The tile and content cannot be empty");
        }
        Article article = new Article();
        article.setTitle(d.getTitle());
        article.setContent(d.getContent());
        article.setUserId(userId);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setSummary(d.getSummary());
        articleService.save(article);
        return Result.success();
    }

    @Override
    public Result listArticles(RequestDTO<Object, Object> requestDTO) {
        List<ArticleVO> vos = new ArrayList<>();
        Page page = requestDTO.getPage();
        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
        List<Article> all = articleService.getAll();
        for (Article article : all) {
            ArticleVO vo = new ArticleVO();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setCreateTime(DateTimeUtils.toDate(article.getCreateTime()));
            vos.add(vo);
        }
        PageInfo<Article> pageInfo = new PageInfo<>(all);

        PageResult<ArticleVO> pageResult = new PageResult<>(vos,pageInfo.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result getArticleDetail(RequestDTO<GetArticleDetailQuery, Object> requestDTO) {
        GetArticleDetailQuery q = requestDTO.getQ();
        if (q==null||q.getId()==null){
            return Result.badArgument();
        }
        Article byId = articleService.getById(q.getId());
        if (byId==null){
            return Result.fail("文章被删除啦~");
        }
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        ArticleDetailVO.Meta meta = new ArticleDetailVO.Meta();
        articleDetailVO.setMeta(meta);
        articleDetailVO.setTitle(byId.getTitle());
        articleDetailVO.setContent(byId.getContent());
        articleDetailVO.setCreateTime(DateTimeUtils.toDate(byId.getCreateTime()));
        articleDetailVO.setUpdateTime(DateTimeUtils.toDate(byId.getUpdateTime()));
        articleDetailVO.setAuthor("test");
        return Result.success(articleDetailVO);
    }
}
