package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.TestData;
import com.chrisfrill.messaging.domain.model.MessageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveHashOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class MessageServiceTest extends TestData {
    @Autowired
    MessageService messageService;

    @MockBean
    ReactiveHashOperations<String, String, MessageEntity> hashOperations;

    @Test
    public void list_GetsMessagesFromRedis_OnValidMessage() {
        given(hashOperations.values(anyString())).willReturn(Flux.fromIterable(List.of(messageEntity)));
        Flux<MessageEntity> savedMessages = messageService.findAll();
        assertEquals(List.of(messageEntity), savedMessages.collectList().block());
    }

    @Test
    public void list_GetsNoMessagesFromRedis_OnNoMessages() {
        given(hashOperations.values(anyString())).willReturn(Flux.fromIterable(List.of()));
        Flux<MessageEntity> savedMessages = messageService.findAll();
        assertEquals(List.of(), savedMessages.collectList().block());
    }

    @Test
    public void save_SavesMessageToRedis_OnValidMessage() {
        given(hashOperations.put(anyString(), anyString(), any(MessageEntity.class))).willReturn(Mono.just(true));
        Mono<MessageEntity> saved = messageService.save(messageEntity);
        assertEquals(messageEntity, saved.block());
    }

    @Test
    public void save_ThrowsIllegalArgumentException_OnNull() {
        assertThrows(IllegalArgumentException.class, () -> messageService.save(null));
    }
}