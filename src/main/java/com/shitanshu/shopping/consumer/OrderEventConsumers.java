package com.shitanshu.shopping.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;

@Service
public class OrderEventConsumers {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumers.class);

    // 3. NOTIFICATION CONSUMER GROUP
    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-notification-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderNotification(OrderPlacedEvent event, Acknowledgment ack) {
        try {
            log.info("[NOTIFICATION-SERVICE] Received OrderPlacedEvent for orderId: {}. Dispatching confirmation to: {}", 
                    event.getOrderId(), event.getUserEmail());
            // Async email/SMS dispatch simulation
            log.info("[NOTIFICATION-SERVICE] Confirmation notification sent to {}", event.getUserEmail());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[NOTIFICATION-SERVICE] Notification dispatch failed for orderId: {}", event.getOrderId(), e);
            throw e;
        }
    }

    // 4. DEAD LETTER QUEUE (DLQ) CONSUMER
    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_DLQ_TOPIC,
        groupId = "shopease-dlq-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleDeadLetterMessages(Object failedEvent, Acknowledgment ack) {
        log.error("[DLQ-MONITOR] Alert! Failed message routed to Dead Letter Topic: {}", failedEvent);
        ack.acknowledge();
    }
}
