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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.backoff.FixedBackOff;

@SpringJUnitConfig(KafkaRetryDlqIntegrationTest.TestConfig.class)
@EmbeddedKafka(
    partitions = 1,
    topics = {
        KafkaRetryDlqIntegrationTest.TEST_TOPIC,
        KafkaRetryDlqIntegrationTest.DLQ_TOPIC
    }
)
class KafkaRetryDlqIntegrationTest {

    static final String TEST_TOPIC = "shopease-retry-test";
    static final String DLQ_TOPIC = "shopease-retry-test-dlt";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Configuration
    static class TestConfig {
    }

    @Test
    void failedMessageShouldRetryThreeTimesAndReachDlq() throws Exception {

        String bootstrapServers = embeddedKafka.getBrokersAsString();

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerFactory<String, String> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps);

        KafkaTemplate<String, String> kafkaTemplate =
                new KafkaTemplate<>(producerFactory);

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "retry-test-" + UUID.randomUUID());
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

        containerProperties.setGroupId("retry-listener-" + UUID.randomUUID());

        MessageListener<String, String> listener = record -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("Intentional test failure");
        };

        containerProperties.setMessageListener(listener);

        KafkaMessageListenerContainer<String, String> container =
                new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        container.setCommonErrorHandler(errorHandler);

        Consumer<String, String> dlqConsumer = null;

        try {
            container.start();
            ContainerTestUtils.waitForAssignment(container, 1);

            Map<String, Object> dlqConsumerProps = new HashMap<>(consumerProps);
            dlqConsumerProps.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    "dlq-reader-" + UUID.randomUUID()
            );

            dlqConsumer = new DefaultKafkaConsumerFactory<String, String>(dlqConsumerProps)
                    .createConsumer();

            TopicPartition dlqPartition = new TopicPartition(DLQ_TOPIC, 0);
            dlqConsumer.assign(List.of(dlqPartition));
            dlqConsumer.seekToBeginning(List.of(dlqPartition));

            String payload = "shopease-retry-verification";

            kafkaTemplate.send(TEST_TOPIC, "test-key", payload)
                    .get(10, TimeUnit.SECONDS);

            ConsumerRecord<String, String> dlqRecord = null;
            long deadline = System.nanoTime() + Duration.ofSeconds(30).toNanos();

            while (System.nanoTime() < deadline && dlqRecord == null) {
                var records = dlqConsumer.poll(Duration.ofMillis(500));

                if (!records.isEmpty()) {
                    dlqRecord = records.iterator().next();
                }
            }

            assertNotNull(dlqRecord, "Message was not published to the DLQ");
            assertEquals(payload, dlqRecord.value(), "DLQ payload mismatch");

            long attemptsDeadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();

            while (attempts.get() < 4 && System.nanoTime() < attemptsDeadline) {
                Thread.sleep(100);
            }

            assertEquals(
                    4,
                    attempts.get(),
                    "Expected one initial attempt and exactly three retries"
            );

        } finally {
            if (dlqConsumer != null) {
                dlqConsumer.close();
            }

            container.stop();
            kafkaTemplate.destroy();
        }
    }
}
