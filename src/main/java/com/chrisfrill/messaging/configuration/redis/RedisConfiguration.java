package com.chrisfrill.messaging.configuration.redis;

import com.chrisfrill.messaging.configuration.redis.converter.BytesToOffsetDateTimeConverter;
import com.chrisfrill.messaging.configuration.redis.converter.OffsetDateTimeToBytesConverter;
import com.chrisfrill.messaging.configuration.redis.converter.OffsetDateTimeToStringConverter;
import com.chrisfrill.messaging.domain.model.MessageEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisProperties redisProperties) {
        return new LettuceConnectionFactory(
                redisProperties.getRedisHost(),
                redisProperties.getRedisPort());
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(OffsetDateTimeToBytesConverter localDateTimeToBytes,
                                                         BytesToOffsetDateTimeConverter bytesToTimestamp,
                                                         OffsetDateTimeToStringConverter localDateTimeToString) {
        return new RedisCustomConversions(Arrays.asList(localDateTimeToBytes, bytesToTimestamp, localDateTimeToString));
    }

    @Bean
    public ReactiveRedisOperations<String, MessageEntity> redisOperations(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        Jackson2JsonRedisSerializer<MessageEntity> valueSerializer = new Jackson2JsonRedisSerializer<>(MessageEntity.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        valueSerializer.setObjectMapper(mapper);

        RedisSerializationContext<String, MessageEntity> serializationContext = RedisSerializationContext
                .<String, MessageEntity>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

    @Bean
    public ReactiveHashOperations<String, String, MessageEntity> hashOperations(ReactiveRedisOperations<String, MessageEntity> redisOperations) {
        return redisOperations.opsForHash();
    }
}