package com.example.postservice.bus;


import com.example.postservice.data.UserEvent;
import com.example.postservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
@Component("createdUserConsumer")
public class CreatedUserConsumer implements Consumer<Message<UserEvent>> {
    private final UserService userService;

    public CreatedUserConsumer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void accept(Message<UserEvent> message) {
        UserEvent userEvent = message.getPayload();
        MessageHeaders messageHeaders = message.getHeaders();
        log.info("User event with id '{}' and email '{}' received from bus. topic: {}, partition: {}, offset: {}, deliveryAttempt: {}",
                userEvent.getId(),
                userEvent.getEmail(),
                messageHeaders.get(KafkaHeaders.RECEIVED_TOPIC, String.class),
                messageHeaders.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class),
                messageHeaders.get(KafkaHeaders.OFFSET, Long.class),
                messageHeaders.get(IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, AtomicInteger.class));
        var user = userService.createUser(userEvent);
        log.info("User with id {} saved.", user.getId());
    }
}
