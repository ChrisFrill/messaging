package com.chrisfrill.messaging.configuration.http;

import com.chrisfrill.messaging.TestData;
import com.chrisfrill.messaging.domain.MessageService;
import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import com.chrisfrill.messaging.domain.model.dto.MessageResponse;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
public class EndpointConfigurationTest extends TestData {
    @MockBean
    private MessageService messageService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getMessages_ReturnEmptyList_IfNonIsSet() {
        Flux<MessageEntity> messageEntityFlux = Flux.fromIterable(List.of());
        given(messageService.findAll()).willReturn(messageEntityFlux);

        webTestClient.get()
                .uri("/messages")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MessageResponse.class)
                .isEqualTo(List.of());
    }

    @Test
    public void getMessages_ReturnCorrectMessages() {
        ModelMapper modelMapper = new ModelMapper();

        List<MessageEntity> messageEntities = List.of(messageEntity, secondMessageEntity);
        Flux<MessageEntity> messageEntityFlux = Flux.fromIterable(List.of(messageEntity, secondMessageEntity));
        List<MessageResponse> messageResponses = messageEntities.stream().map(entity ->
                modelMapper.map(entity, MessageResponse.class)).collect(Collectors.toList());

        given(messageService.findAll()).willReturn(messageEntityFlux);

        webTestClient.get()
                .uri("/messages")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MessageResponse.class)
                .isEqualTo(messageResponses);
    }

    @Test
    public void postMessage_ReturnOk_IfMessageIsValid() {
        given(messageService.save(any(MessageEntity.class))).willReturn(Mono.just(messageEntity));

        webTestClient.post()
                .uri("/messages")
                .body(Mono.just(messageRequest), MessageRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void postMessage_ReturnBadRequest_IfMessageIsInvalid() {
        Flux<MessageEntity> messageEntityFlux = Flux.fromIterable(List.of());
        given(messageService.save(any(MessageEntity.class))).willReturn(Mono.just(messageEntity));

        webTestClient.post()
                .uri("/messages")
                .body(Mono.just(invalidMessageRequest), MessageRequest.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}