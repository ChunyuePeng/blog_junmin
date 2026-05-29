package org.example.junmin.component;

import jakarta.annotation.Resource;
import org.example.junmin.comm.dto.BlogSyncMessage;
import org.example.junmin.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MQSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void syncBlog(BlogSyncMessage blogSyncMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                blogSyncMessage
        );
    }
}
