package com.chrisfrill.messaging.configuration.http;

import com.chrisfrill.messaging.domain.HttpMessageHandler;
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
    private final HttpMessageHandler httpMessageHandler;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route()
                .GET("/messages", accept(APPLICATION_JSON), httpMessageHandler::findAll)
                .POST("/messages", accept(APPLICATION_JSON), httpMessageHandler::save)
                .build();
    }
}
