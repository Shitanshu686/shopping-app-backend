package com.shitanshu.orderservice.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public OrderEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {

        eventPublisher.publishEvent(event);

        System.out.println(
                "ORDER_CREATED event published for Order ID: "
                        + event.getOrderId()
        );
    }
}
