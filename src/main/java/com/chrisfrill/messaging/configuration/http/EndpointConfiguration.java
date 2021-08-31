package com.chrisfrill.messaging.configuration.http;

import com.chrisfrill.messaging.domain.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class EndpointConfiguration {
    private final MessageHandler messageHandler;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route()
                .GET("/messages", accept(APPLICATION_JSON), messageHandler::findAll)
                .POST("/messages", accept(APPLICATION_JSON), messageHandler::save)
                .build();
    }
}
