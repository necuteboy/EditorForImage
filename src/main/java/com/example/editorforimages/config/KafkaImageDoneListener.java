package com.example.editorforimages.config;

import com.example.editorforimages.entity.ImageDoneMessage;
import com.example.editorforimages.service.RequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.enable", havingValue = "true")
public class KafkaImageDoneListener {

    private final RequestService requestService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = {"${spring.kafka.topic-name.images-done}"}, autoStartup = "true")
    public void listen(final ConsumerRecord<?, ?> cr,
                       final Acknowledgment ack) {
        try {
            var message = mapper.readValue(cr.value().toString(), ImageDoneMessage.class);
            requestService.closeRequest(message.getRequestId(), message.getImageId());
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Unable to map String->ImageDoneMessage", e);
        }
    }

}
