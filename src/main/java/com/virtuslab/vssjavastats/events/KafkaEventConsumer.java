package com.virtuslab.vssjavastats.events;

import com.virtuslab.vssjavastats.domain.StatsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventConsumer {

    private final StatsRepository repository;

    public KafkaEventConsumer(StatsRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "{spring.kafka.group-id}")
    public void storeEvent(@Payload String message) {
        System.out.println("Received Message: " + message);
        final var passwordSavedEvent = PasswordDeserializer.deserializePassword(message);
        repository.save(passwordSavedEvent.toPassword());
    }
}