package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.TestData;
import com.chrisfrill.messaging.domain.model.MessageEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MessageServiceTest extends TestData {
    @Autowired
    MessageService messageService;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    public void list_GetsMessagesFromRedis_OnValidMessage() {
        given(messageRepository.findAll()).willReturn(List.of(messageEntity));

        Flux<MessageEntity> savedMessages = messageService.findAll();
        assertEquals(List.of(messageEntity), savedMessages.collectList().block());
    }

    @Test
    public void List_GetsNoMessagesFromRedis_OnNoMessages() {
        given(messageRepository.findAll()).willReturn(List.of());

        Flux<MessageEntity> savedMessages = messageService.findAll();
        assertEquals(List.of(), savedMessages.collectList().block());
    }

    @Test
    public void Save_SavesMessageToRedis_OnValidMessage() {
        given(messageRepository.save(any(MessageEntity.class))).willReturn(messageEntity);
        Mono<MessageEntity> saved = messageService.save(messageEntity);
        assertEquals(messageEntity, saved.block());
    }

    @Test
    public void Save_ThrowsIllegalArgumentException_OnNull() {
        given(messageRepository.save(null)).willThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> messageService.save(null));
    }
}