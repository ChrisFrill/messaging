package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import com.chrisfrill.messaging.domain.model.dto.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Log4j2
@Component
@RequiredArgsConstructor
public class HttpMessageHandler {
    private final MessageRequestValidator validator;
    private final MessageService messageService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper;

    /**
     * Returns all messages from the underlying persistent storage inside the HTTP response's body
     *
     * @return a Mono that emits a ServerResponse that represents the HTTP response
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return defaultReadResponse(messageService.findAll()
                .map(message -> modelMapper.map(message, MessageResponse.class))
        );
    }

    /**
     * Save a new message to the underlying persistent storage from an HTTP request
     *
     * @return a Mono that emits a ServerResponse that represents the HTTP response
     */
    public Mono<ServerResponse> save(ServerRequest request) {
        return defaultWriteResponse(request.bodyToMono(MessageRequest.class)
                .flatMap(message -> {
                    validateRequest(message);
                    String json = toJson(message);
                    return messageService.save(modelMapper.map(message, MessageEntity.class))
                            .doOnSuccess(m -> sendMessageToWebSocket(json));
                })
        );
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<MessageResponse> message) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message, MessageResponse.class)
                .onErrorResume(e -> Mono.error(new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Couldn't get messages from Redis", e)));
    }

    private Mono<ServerResponse> defaultWriteResponse(Publisher<MessageEntity> message) {
        return Mono
                .from(message)
                .flatMap(savedMessage -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "*")
                        .bodyValue(savedMessage))
                        .onErrorResume(e -> Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Couldn't send request to server", e)));
    }

    private void validateRequest(MessageRequest message) {
        Errors errors = new BeanPropertyBindingResult(message, MessageRequest.class.getName());
        validator.validate(message, errors);

        if (!(errors.getAllErrors().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
        }
    }

    private String toJson(MessageRequest message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void sendMessageToWebSocket(String json) {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create("ws://localhost:8080/message"),
                session ->
                        session.send(Mono.just(session.textMessage(json)))
        ).subscribe();
        log.info("New message sent to WebSocket");
    }

}
