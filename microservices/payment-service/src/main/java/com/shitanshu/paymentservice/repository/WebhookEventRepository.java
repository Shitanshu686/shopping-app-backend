package com.shitanshu.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.paymentservice.model.WebhookEvent;

public interface WebhookEventRepository
        extends JpaRepository<WebhookEvent, Integer> {

    Optional<WebhookEvent> findByEventId(String eventId);
}
