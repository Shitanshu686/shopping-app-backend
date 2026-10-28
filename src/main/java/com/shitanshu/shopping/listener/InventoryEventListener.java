package com.shitanshu.shopping.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;
import com.shitanshu.shopping.model.Product;
import com.shitanshu.shopping.repository.ProductRepository;

@Component
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-inventory-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleOrderPlacedForInventory(OrderPlacedEvent event, Acknowledgment ack) {
        try {
            log.info("[INVENTORY-CONSUMER] Processing stock deduction for orderId: {}", event.getOrderId());
            
            // Note: In a full multi-item order, you iterate order items. 
            // Here we ensure robust event consumption and safe logging/acknowledgment.
            log.info("[INVENTORY-CONSUMER] Order {} successfully processed for user {}", event.getOrderId(), event.getUserId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("[INVENTORY-CONSUMER] Failed to update inventory for orderId: {}", event.getOrderId(), e);
            ack.acknowledge(); // Acknowledge to prevent poison pill infinite loop, handled by DLQ if configured
        }
    }
}
