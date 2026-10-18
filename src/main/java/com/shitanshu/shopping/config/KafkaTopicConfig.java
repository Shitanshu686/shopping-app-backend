package com.shitanshu.shopping.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ORDER_PLACED_TOPIC = "order-placed-topic";
    public static final String ORDER_DLQ_TOPIC = "order-placed-topic-dlt";

    @Bean
    public NewTopic orderPlacedTopic() {
        return TopicBuilder.name(ORDER_PLACED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderPlacedDlqTopic() {
        return TopicBuilder.name(ORDER_DLQ_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
