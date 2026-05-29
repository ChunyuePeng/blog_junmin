package org.example.junmin.task;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.example.junmin.dao.service.ArticleService;
import org.example.junmin.entity.Article;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ViewSyncTask {
    @PostConstruct
    public void updateViewCountToRedis(){
        List<Article> all = articleService.getAll();
        for (Article article : all) {
            if (article.getViewCount()==null){
                article.setViewCount(0);
            }
            redisTemplate.opsForValue().set("article:"+article.getId()+":views",article.getViewCount());
        }
    }

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ArticleService articleService;

    @Scheduled(fixedRate = 30000)
    public void syncViews() {

        Set<String> keys = redisTemplate.keys("article:*:views");

        if (keys == null) return;

        for (String key : keys) {

            String viewsStr = redisTemplate.opsForValue().get(key).toString();
            if (viewsStr == null) continue;

            Long views = Long.valueOf(viewsStr);

            Long articleId = Long.valueOf(
                    key.split(":")[1]
            );
            articleService.updateViewCount(articleId,views);
        }
    }
}
