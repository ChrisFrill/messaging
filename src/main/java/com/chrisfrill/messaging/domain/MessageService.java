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

    public Mono<MessageEntity> save(MessageEntity messageEntity) {
        log.info("Saving new message to Redis");
        return Mono.just(messageRepository.save(messageEntity));
    }

    public Flux<MessageEntity> findAll() {
        log.info("Getting messages from Redis");
        return Flux.fromIterable(messageRepository.findAll());
    }
}
