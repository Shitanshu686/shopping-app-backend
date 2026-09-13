package com.shitanshu.orderservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "order-service-group"
    )
    public void consumeOrderCreated(OrderCreatedEvent event) {

        System.out.println(
                "ORDER_CREATED event received from Kafka: "
                + "Order ID=" + event.getOrderId()
                + ", Email=" + event.getEmail()
                + ", Total=" + event.getTotalAmount()
        );
    }
}
