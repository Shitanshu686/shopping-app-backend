package com.shitanshu.shopping.audit.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shitanshu.shopping.audit.service.AuditLogService;
import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;

@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-audit-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleOrderPlacedAudit(OrderPlacedEvent event, Acknowledgment ack) {
        try {
            log.info("[AUDIT-CONSUMER] Processing audit trail for order: {}", event.getOrderId());
            String payloadJson = objectMapper.writeValueAsString(event);
            
            auditLogService.recordLogAsync(
                "ORDER",
                String.valueOf(event.getOrderId()),
                "PLACED",
                payloadJson,
                Long.valueOf(event.getUserId())
            );

            ack.acknowledge();
        } catch (Exception e) {
            log.error("[AUDIT-CONSUMER] Failed to ingest audit trail for orderId: {}", event.getOrderId(), e);
            ack.acknowledge(); // Logged failure, avoid blocking partition
        }
    }
}
