package com.shitanshu.shopping.producer;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
        
        String correlationId = MDC.get("correlationId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            KafkaTopicConfig.ORDER_PLACED_TOPIC, 
            String.valueOf(event.getOrderId()), 
            event
        );
        
        if (correlationId != null && !correlationId.isBlank()) {
            record.headers().add("correlationId", correlationId.getBytes(StandardCharsets.UTF_8));
        }
        
        this.kafkaTemplate.send(record);
    }
}
