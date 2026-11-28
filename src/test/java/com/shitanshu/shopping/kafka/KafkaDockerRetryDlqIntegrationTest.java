package com.shitanshu.shopping.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.util.backoff.FixedBackOff;

class KafkaDockerRetryDlqIntegrationTest {

    private static final String TEST_TOPIC = "shopease-docker-retry-test";
    private static final String DLQ_TOPIC = "shopease-docker-retry-test-dlt";

    @Test
    @EnabledIfSystemProperty(
        named = "kafka.docker.integration",
        matches = "true"
    )
    void failedMessageShouldRetryThreeTimesAndReachDockerDlq() throws Exception {

        String bootstrapServers = System.getProperty(
                "kafka.docker.bootstrap-servers", "localhost:9092");

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.ACKS_CONFIG, "all");

        ProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps);

        KafkaTemplate<String, String> kafkaTemplate =
                new KafkaTemplate<>(producerFactory);

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "docker-retry-" + UUID.randomUUID());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps);

        AtomicInteger attempts = new AtomicInteger();

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) ->
                                new TopicPartition(DLQ_TOPIC, record.partition())
                );

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3));

        ContainerProperties containerProperties =
                new ContainerProperties(TEST_TOPIC);

        containerProperties.setGroupId("docker-retry-listener-" + UUID.randomUUID());

        MessageListener<String, String> listener = record -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("Intentional Docker retry test failure");
        };

        containerProperties.setMessageListener(listener);

        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        container.setCommonErrorHandler(errorHandler);

        Consumer<String, String> dlqConsumer = null;

        try {
            container.start();
            org.springframework.kafka.test.utils.ContainerTestUtils
                    .waitForAssignment(container, 1);

            Map<String, Object> dlqProps = new HashMap<>(consumerProps);
            dlqProps.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    "docker-dlq-reader-" + UUID.randomUUID()
            );

            dlqConsumer =
                    new DefaultKafkaConsumerFactory<String, String>(dlqProps)
                            .createConsumer();

            TopicPartition dlqPartition = new TopicPartition(DLQ_TOPIC, 0);
            dlqConsumer.assign(List.of(dlqPartition));
            dlqConsumer.seekToEnd(List.of(dlqPartition));

            String payload = "docker-retry-verification-" + UUID.randomUUID();

            kafkaTemplate.send(TEST_TOPIC, "docker-test-key", payload)
                    .get(15, TimeUnit.SECONDS);

            ConsumerRecord<String, String> dlqRecord = null;
            long deadline = System.nanoTime() + Duration.ofSeconds(40).toNanos();

            while (System.nanoTime() < deadline && dlqRecord == null) {
                var records = dlqConsumer.poll(Duration.ofMillis(500));
                if (!records.isEmpty()) {
                    dlqRecord = records.iterator().next();
                }
            }

            assertNotNull(dlqRecord, "Docker Kafka DLQ did not receive the failed message");
            assertEquals(payload, dlqRecord.value(), "Docker DLQ payload mismatch");
            assertEquals(4, attempts.get(),
                    "Expected one initial attempt and exactly three retries");

        } finally {
            if (dlqConsumer != null) {
                dlqConsumer.close();
            }
            container.stop();
            kafkaTemplate.destroy();
        }
    }
}
