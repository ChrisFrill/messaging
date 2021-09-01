package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.TestData;
import com.chrisfrill.messaging.TestRedisConfiguration;
import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableScheduling
@SpringBootTest(classes = TestRedisConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketMessageHandlerTest extends TestData {
    Boolean isScheduled = false;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void webSocket_GetsCorrectMessage_OnMessageRequest() throws InterruptedException {
        WebSocketClient socketClient = new ReactorNettyWebSocketClient();
        List<MessageRequest> messages = new ArrayList<>();

        socketClient.execute(
                URI.create("ws://localhost:8080/message"),
                session -> session.receive().map(WebSocketMessage::getPayloadAsText)
                        .doOnNext(message -> {
                            try {
                                messages.add(objectMapper.readValue(message, MessageRequest.class));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }).then()
        ).subscribe();

        write(messageRequest);

        Thread.sleep(5000);
        assertEquals(List.of(messageRequest), messages);
    }

    @Test
    public void webSocket_GetsMultipleMessages_OnMultipleMessagesSent() throws Exception {
        isScheduled = true;
        WebSocketClient socketClient = new ReactorNettyWebSocketClient();
        AtomicLong counter = new AtomicLong();

        socketClient.execute(
                URI.create("ws://localhost:8080/message"),
                session -> session.receive().map(WebSocketMessage::getPayloadAsText)
                        .doOnNext(str -> {
                            System.out.println("Counter: " + counter);
                            counter.incrementAndGet();
                        })
                        .then()
        ).subscribe();

        Thread.sleep(5000);
        assertThat(counter.get(), greaterThan(3L));
    }

    private MessageRequest generateRandomMessage() {
        return new MessageRequest(UUID.randomUUID().toString(), dateTime);
    }

    private void write(MessageRequest messageRequest) {
        WebClient webClient = WebClient.builder().build();
        webClient
                .post()
                .uri("http://localhost:8080/messages")
                .body(Mono.just(messageRequest), MessageRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .thenReturn(messageResponse)
                .subscribe();
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 300)
    private void writeScheduled() {
        if (isScheduled) {
            write(generateRandomMessage());
        }
    }

}