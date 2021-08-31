package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessageWebSocketHandler implements WebSocketHandler {
    private final ObjectMapper objectMapper;
    private final Sinks.Many<MessageEntity> messagePublisher = Sinks.many().replay().all();
    private final Flux<MessageEntity> messages = messagePublisher.asFlux().replay(0).autoConnect();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> outputMessages = Flux.from(messages).map(this::toJSON);
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toMessage)
                .doOnNext(this::onNext)
                .zipWith(session.send(outputMessages.map(session::textMessage)))
                .then();
    }

    private MessageEntity toMessage(String json) {
        try {
            return objectMapper.readValue(json, MessageEntity.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON was provided: " + json, e);
        }
    }

    private String toJSON(MessageEntity event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void onNext(MessageEntity message) {
        messagePublisher.tryEmitNext(message);
    }
}
