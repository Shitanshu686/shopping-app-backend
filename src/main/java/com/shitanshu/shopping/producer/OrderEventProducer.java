package com.shitanshu.shopping.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;

@Service
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Publishing OrderPlacedEvent to Kafka topic '{}': {}", KafkaTopicConfig.ORDER_PLACED_TOPIC, event);
        this.kafkaTemplate.send(KafkaTopicConfig.ORDER_PLACED_TOPIC, String.valueOf(event.getOrderId()), event);
    }
}
