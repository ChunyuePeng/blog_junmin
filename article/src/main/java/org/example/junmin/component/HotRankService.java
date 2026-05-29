package org.example.junmin.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HotRankService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HOT_KEY = "hot:article";

    /**
     * 增加热度
     */
    public void addScore(Long articleId, double score) {

        redisTemplate.opsForZSet()
                .incrementScore(
                        HOT_KEY,
                        articleId,
                        score
                );
    }


    public Set<ZSetOperations.TypedTuple<Object>> top10() {

        return redisTemplate.opsForZSet()
                .reverseRangeWithScores(
                        HOT_KEY,
                        0,
                        9
                );
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void decayHotScore() {

        Set<Object> ids = redisTemplate.opsForZSet()
                .range(HOT_KEY, 0, -1);

        for (Object id : ids) {

            Double score = redisTemplate.opsForZSet()
                    .score(HOT_KEY, id);

            if (score != null) {

                redisTemplate.opsForZSet()
                        .add(
                                HOT_KEY,
                                id,
                                score * 0.8
                        );
            }
        }
    }
}
