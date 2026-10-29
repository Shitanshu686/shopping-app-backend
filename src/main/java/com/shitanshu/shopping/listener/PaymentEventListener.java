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
import com.shitanshu.shopping.repository.PaymentRepository;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @KafkaListener(
        topics = KafkaTopicConfig.ORDER_PLACED_TOPIC,
        groupId = "shopease-payment-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleOrderPlacedForPayment(OrderPlacedEvent event, Acknowledgment ack) {
        try {
            log.info("[PAYMENT-CONSUMER] Processing payment settlement for orderId: {} with amount: {}", 
                    event.getOrderId(), event.getTotalAmount());
            
            // Simulating secure payment processing gateway verification
            log.info("[PAYMENT-CONSUMER] Payment verified & recorded successfully for user: {}", event.getUserId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("[PAYMENT-CONSUMER] Payment processing failed for orderId: {}", event.getOrderId(), e);
            ack.acknowledge();
        }
    }
}
