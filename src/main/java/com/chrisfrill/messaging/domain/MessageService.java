package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {
    private final ReactiveHashOperations<String, String, MessageEntity> hashOperations;

    /**
     * Save a new message to the underlying persistent storage.
     *
     * @return a Mono that emits the saved message
     */
    public Mono<MessageEntity> save(MessageEntity messageEntity) {
        if (messageEntity == null) {
            throw new IllegalArgumentException("No message was provided for saving");
        }
        log.info("Saving new message to database");
        log.info("Message: " + messageEntity);
        return hashOperations.put("MESSAGES", UUID.randomUUID().toString(), messageEntity).thenReturn(messageEntity);
    }

    /**
     * Return all messages from the underlying persistent storage.
     *
     * @return a Flux that emits the persisted messages from the underlying persistent storage
     */
    public Flux<MessageEntity> findAll() {
        log.info("Getting messages from database");
        return hashOperations.values("MESSAGES");
    }
}
