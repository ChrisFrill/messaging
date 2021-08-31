package com.chrisfrill.messaging.domain;

import com.chrisfrill.messaging.domain.model.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, String> {
}