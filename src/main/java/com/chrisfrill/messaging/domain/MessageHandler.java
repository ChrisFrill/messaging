package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.chrisfrill.messaging.domain.model.dto.MessageRequest;
import com.chrisfrill.messaging.domain.model.dto.MessageResponse;
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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final ModelMapper modelMapper = new ModelMapper();

    private final MessageService messageService;

    private final MessageRequestValidator validator;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return defaultReadResponse(messageService.findAll()
                .map(message -> modelMapper.map(message, MessageResponse.class))
        );
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return defaultWriteResponse(request.bodyToMono(MessageRequest.class)
                .flatMap(message -> {
                    validateRequest(message);
                    return messageService.save(modelMapper.map(message, MessageEntity.class));
                })
        );
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<MessageResponse> message) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message, MessageResponse.class)
                .onErrorResume(e -> Mono.just("Error " + e.getMessage())
                        .flatMap(s -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .bodyValue(s))
                );
    }

    private Mono<ServerResponse> defaultWriteResponse(Publisher<MessageEntity> message) {
        return Mono
                .from(message)
                .flatMap(savedMessage -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Allow-Headers", "*")
                        .bodyValue(savedMessage)
                        .onErrorResume(e -> Mono.just("Error " + e.getMessage())
                                .flatMap(s -> ServerResponse.ok()
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .bodyValue(s)))
                );
    }

    private void validateRequest(MessageRequest message) {
        Errors errors = new BeanPropertyBindingResult(message, MessageRequest.class.getName());
        validator.validate(message, errors);

        if (!(errors.getAllErrors().isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getAllErrors().toString());
        }
    }
}
