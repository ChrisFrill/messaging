package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    /**
     * Save a new message to the underlying persistent storage.
     *
     * @return a Mono that emits the saved message
     */
    public Mono<MessageEntity> save(MessageEntity messageEntity) {
        log.info("Saving new message to database");
        return Mono.just(messageRepository.save(messageEntity));
    }

    /**
     * Return all messages from the underlying persistent storage.
     *
     * @return a Flux that emits the persisted messages from the underlying persistent storage
     */
    public Flux<MessageEntity> findAll() {
        log.info("Getting messages from database");
        return Flux.fromIterable(messageRepository.findAll());
    }
}
