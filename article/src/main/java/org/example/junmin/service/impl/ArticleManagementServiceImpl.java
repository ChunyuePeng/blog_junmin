package org.example.junmin.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.example.junmin.comm.dto.BlogSyncMessage;
import org.example.junmin.comm.query.BlogSearchQuery;
import org.example.junmin.comm.query.GetArticleDetailQuery;
import org.example.junmin.comm.vo.ArticleDetailVO;
import org.example.junmin.comm.vo.ArticleSearchVO;
import org.example.junmin.comm.vo.Top10VO;
import org.example.junmin.component.HotRankService;
import org.example.junmin.component.MQSender;
import org.example.junmin.config.UserContext;
import org.example.junmin.dao.service.ArticleService;
import org.example.junmin.dto.ArticleDTO;
import org.example.junmin.dto.Page;
import org.example.junmin.dto.RequestDTO;
import org.example.junmin.entity.Article;
import org.example.junmin.es.document.BlogDocument;
import org.example.junmin.service.ArticleManagementService;
import org.example.junmin.util.DateTimeUtils;
import org.example.junmin.util.JsonUtil;
import org.example.junmin.util.KeywordExtractor;
import org.example.junmin.util.MarkdownSegmentUtil;
import org.example.junmin.vo.PageResult;
import org.example.junmin.vo.Result;
import org.example.junmin.vo.article.ArticleVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleManagementServiceImpl implements ArticleManagementService {
    @Value("${blog.hostname}")
    private String hostname;
    @Value("${blog.image-path}")
    private String imagePath;
    @Resource
    private ArticleService articleService;
    @Resource
    private MQSender mqSender;
    @Resource
    private ElasticsearchClient client;
    @Resource
    private HotRankService hotRankService;
    @Resource
    private RedisTemplate redisTemplate;
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
        List<String> segment = MarkdownSegmentUtil.segment(d.getContent());
        List<String> strings = KeywordExtractor.topKeywords(segment, 3);
        Article article = new Article();
        article.setKeywords(JsonUtil.toJson(strings));
        article.setTitle(d.getTitle());
        article.setContent(d.getContent());
        article.setUserId(userId);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        article.setSummary(d.getSummary());
        articleService.save(article);
        BlogSyncMessage blogSyncMessage = new  BlogSyncMessage();
        blogSyncMessage.setBlogId(article.getId());
        blogSyncMessage.setEventType("CREATE");
        mqSender.syncBlog(blogSyncMessage);
        return Result.success();
    }

    public static String cleanMarkdown(String md) {

        return md
                // 标题
                .replaceAll("#+\\s*", "")
                // 加粗/斜体
                .replaceAll("\\*\\*|__", "")
                // 代码块
                .replaceAll("`+", "")
                // 链接
                .replaceAll("\\[(.*?)]\\(.*?\\)", "$1")
                // 图片
                .replaceAll("!\\[(.*?)]\\(.*?\\)", "$1")
                // 多余符号
                .replaceAll("[*_>\\-]", "")
                .trim();
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
            vo.setViews(article.getViewCount());
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
        hotRankService.addScore(q.getId(),1);
        increaseViews(q.getId());
        return Result.success(articleDetailVO);
    }

    public Long increaseViews(Long articleId) {
        String key = "article:" + articleId + ":views";
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Result getTagList() {
        List<Article> all = articleService.getAll();
        List<String> tags = new ArrayList<>();
        for (Article article : all) {
            if (StringUtils.isNotEmpty(article.getKeywords())){
                List<String> c = JsonUtil.fromJson(article.getKeywords(), new TypeReference<>() {
                });
                tags.addAll(c);
            }

        }
        List<String> strings = KeywordExtractor.topKeywords(tags, 3);
        return Result.success(strings);
    }

    @Override
    public Result uploadImage(MultipartFile multipartFile) {
        File path = new File(imagePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(imagePath+File.separator + UUID.randomUUID()+"."+ FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("文件保存失败");
        }

        return Result.success(hostname + "/article-public/get-image?fileName=" + file.getName());
    }

    @Override
    public ResponseEntity<InputStreamResource> getImage(String fileName, HttpServletResponse response) throws FileNotFoundException {
        File file =  new File(imagePath+File.separator+fileName);
        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename="+fileName
                )
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Override
    public Result search(RequestDTO<BlogSearchQuery, Object> keyword) throws IOException {
        BlogSearchQuery q1 = keyword.getQ();
        Pageable pageable = PageRequest.of(keyword.getPage().getCurrentPage()-1,keyword.getPage().getPageSize());
        SearchResponse<BlogDocument> response =
                client.search(s -> s
                                .index("blog")
                                .query(q -> q
                                        .multiMatch(m -> m
                                                .fields("title", "content")
                                                .query(q1.getKeyword())
                                        )
                                ).from(Math.toIntExact(pageable.getOffset()))
                                .size(pageable.getPageSize())
                                .highlight(h -> h
                                        .fields("content", f -> f
                                                .preTags("<mark>")
                                                .postTags("</mark>")
                                                .fragmentSize(150)
                                        )
                                ),
                        BlogDocument.class
                );


        List<ArticleSearchVO> list = new ArrayList<>();
        PageResult<ArticleSearchVO> ans = new PageResult<>(list, response.hits().total().value());
        for (Hit<BlogDocument> hit : response.hits().hits()) {

            BlogDocument doc = hit.source();
            ArticleSearchVO vo = new ArticleSearchVO();
            vo.setId(doc.getId());
            vo.setTitle(doc.getTitle());
            list.add(vo);


            // ===== 摘要逻辑 =====
            String summary;

            Map<String, List<String>> highlight = hit.highlight();

            if (highlight != null && highlight.containsKey("content")) {

                // 有高亮 → 用高亮片段
                summary = highlight.get("content").get(0);

            } else {

                // 没高亮 → 截取前100-150字
                summary = doc.getContent();

                if (summary != null && summary.length() > 150) {
                    summary = summary.substring(0, 150) + "...";
                }
            }

            vo.setSummary(summary);

        }

        return Result.success(ans);
    }

    @Override
    public Result top10() {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = hotRankService.top10();
        List<Top10VO> ans = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
            Top10VO vo = new Top10VO();
            vo.setId(((Integer) typedTuple.getValue()));
            Article byId = articleService.getById(Long.valueOf(vo.getId()));
            vo.setTitle(byId.getTitle());
            vo.setScore(typedTuple.getScore());
            ans.add(vo);
        }
        return Result.success(ans);
    }
}
