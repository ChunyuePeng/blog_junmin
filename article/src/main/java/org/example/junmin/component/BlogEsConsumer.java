package org.example.junmin.component;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import org.example.junmin.comm.dto.BlogSyncMessage;
import org.example.junmin.config.RabbitMQConfig;
import org.example.junmin.dao.service.ArticleService;
import org.example.junmin.entity.Article;
import org.example.junmin.es.document.BlogDocument;
import org.example.junmin.es.repository.BlogRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class BlogEsConsumer {
    @Resource
    private BlogRepository blogRepository;
    @Resource
    private ArticleService articleService;

    @RabbitListener(
            queues = RabbitMQConfig.DLQ
    )
    public void consume(Message message, Channel channel) {
        long tag = message.getMessageProperties()
                .getDeliveryTag();

        try {
            // 1. 记录失败信息

            // 2. TODO: 入库 / 告警 / 人工处理

            // 3. ACK
            channel.basicAck(tag, false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // DLQ不再进入死循环
            try {
                channel.basicReject(tag, false);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumeBlog(BlogSyncMessage msg, Channel channel, Message message) {
        long deliveryTag =
                message.getMessageProperties()
                        .getDeliveryTag();
        try {
            Article byId = articleService.getById(msg.getBlogId());
            if ("CREATE".equals(msg.getEventType())) {
                if (byId!=null) {
                    BlogDocument blogDocument = new BlogDocument();
                    blogDocument.setTitle(byId.getTitle());
                    blogDocument.setContent(byId.getContent());
                    blogDocument.setId(byId.getId());
                    blogRepository.save(blogDocument);
                }
            }
            channel.basicAck(
                    deliveryTag,
                    false
            );
        } catch (IOException e) {
            System.out.println("消费失败");
            try {
                if (msg.getRetryCount()<MAX_RETRY_COUNT){
                    msg.setRetryCount(
                            msg.getRetryCount() + 1
                    );

                    /*
                     * 重新入队
                     */
                    channel.basicNack(
                            deliveryTag,
                            false,
                            true
                    );
                }else {
                    channel.basicReject(
                            deliveryTag,
                            false
                    );
                }
            } catch (IOException ex) {
                System.out.println("消费失败1");
            }
        }


    }

    public static final int MAX_RETRY_COUNT = 3;
}
