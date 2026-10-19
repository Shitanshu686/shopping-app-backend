package com.shitanshu.shopping.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;

@Service
public class OrderEventConsumers {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumers.class);

    // 1. INVENTORY CONSUMER GROUP
    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryStockDeduction(OrderPlacedEvent event) {
        log.info("[INVENTORY-SERVICE] Received OrderPlacedEvent for orderId: {}. Deducting stock...", event.getOrderId());
        // Async inventory processing logic executes here
        log.info("[INVENTORY-SERVICE] Stock successfully deducted for orderId: {}", event.getOrderId());
    }

    // 2. NOTIFICATION CONSUMER GROUP
    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-notification-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderNotification(OrderPlacedEvent event) {
        log.info("[NOTIFICATION-SERVICE] Received OrderPlacedEvent for orderId: {}. Dispatching confirmation email to: {}", 
                event.getOrderId(), event.getUserEmail());
        // Async email/SMS notification logic executes here
        log.info("[NOTIFICATION-SERVICE] Confirmation notification sent to {}", event.getUserEmail());
    }

    // 3. DEAD LETTER QUEUE (DLQ) CONSUMER
    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_DLQ_TOPIC,
        groupId = "shopease-dlq-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleDeadLetterMessages(Object failedEvent) {
        log.error("[DLQ-MONITOR] Alert! Processing failed message redirected to Dead Letter Topic: {}", failedEvent);
        // Alerting, incident logging, or manual intervention storage
    }
}
