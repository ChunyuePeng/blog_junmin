package org.example.junmin.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 交换机
     */
    public static final String EXCHANGE =
            "blog.exchange";

    /**
     * 正常队列
     */
    public static final String QUEUE =
            "blog.sync.queue";

    /**
     * 死信队列
     */
    public static final String DLQ =
            "blog.sync.dlq";

    /**
     * RoutingKey
     */
    public static final String ROUTING_KEY =
            "blog.sync";

    /**
     * 死信 RoutingKey
     */
    public static final String DLQ_ROUTING_KEY =
            "blog.sync.dlq";

    /**
     * Exchange
     */
    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }

    /**
     * 正常队列
     */
    @Bean
    public Queue queue() {

        return QueueBuilder
                .durable(QUEUE)
                .deadLetterExchange(EXCHANGE)
                .deadLetterRoutingKey(
                        DLQ_ROUTING_KEY
                )

                .build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {

        return QueueBuilder
                .durable(DLQ)
                .build();
    }

    /**
     * 正常绑定
     */
    @Bean
    public Binding binding() {

        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    /**
     * 死信绑定
     */
    @Bean
    public Binding dlqBinding() {

        return BindingBuilder
                .bind(deadLetterQueue())
                .to(exchange())
                .with(DLQ_ROUTING_KEY);
    }
}
