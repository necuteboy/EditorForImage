package com.example.editorforimages;

import com.example.editorforimages.entity.ImageWipMessage;
import com.example.editorforimages.kafka.KafkaImageWipProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.enable", havingValue = "false")
public class KafkaImageWipProducerStub extends KafkaImageWipProducer {

    public KafkaImageWipProducerStub() {
        super(null, null);
    }

    @Override
    public void sendMessage(ImageWipMessage message) {
    }

}
