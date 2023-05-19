package com.virtuslab.vssjavastats.support;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KafkaSupport {

    public static KafkaProducer<String, String> createKafkaProducer(KafkaContainer kafkaContainer) {
        return new KafkaProducer<>(
                ImmutableMap.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers()),
                new StringSerializer(),
                new StringSerializer()
        );
    }

    public static KafkaConsumer<String, String> createKafkaConsumer(KafkaContainer kafkaContainer) {
        return new KafkaConsumer<>(
                ImmutableMap.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers(),
                        ConsumerConfig.GROUP_ID_CONFIG, "test.-" + UUID.randomUUID(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                new StringDeserializer()
        );
    }

    public static void createTopic(KafkaContainer kafkaContainer, String topic) {
        AdminClient adminClient = createAdminClient(kafkaContainer);
        while (true) {
            try {
                adminClient.createTopics(List.of(new NewTopic(topic, 1, (short) 1))).all().get();
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Ignoring.");
            }
            break;
        }
        adminClient.close();
    }

    public static void resetKafka(KafkaContainer kafkaContainer) {
        AdminClient adminClient = createAdminClient(kafkaContainer);
        try {
            adminClient.deleteTopics(adminClient.listTopics().names().get()).all().get();
        } catch (Exception e) {
            throw new RuntimeException("Unable to clean Kafka topics", e);
        }
        adminClient.close();
    }

    private static AdminClient createAdminClient(KafkaContainer kafkaContainer) {
        return AdminClient.create(
                Map.of("bootstrap.servers", kafkaContainer.getBootstrapServers())
        );
    }
}
