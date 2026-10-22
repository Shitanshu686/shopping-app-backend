package com.shitanshu.shopping;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.shitanshu.shopping.config.KafkaTopicConfig;
import com.shitanshu.shopping.event.OrderPlacedEvent;
import com.shitanshu.shopping.producer.OrderEventProducer;

@SpringBootTest
public class KafkaMessagingIntegrationTest {

    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Test
    void verifyOrderPlacedEventPublishingPipeline() {
        assertDoesNotThrow(() -> {
            OrderPlacedEvent testEvent = new OrderPlacedEvent(
                9999,
                101,
                "testuser@shopease.com",
                4999.0,
                "PLACED"
            );

            orderEventProducer.publishOrderPlacedEvent(testEvent);

            verify(kafkaTemplate).send(
                eq(KafkaTopicConfig.ORDER_PLACED_TOPIC),
                eq("9999"),
                any(OrderPlacedEvent.class)
            );
        });
    }
}
