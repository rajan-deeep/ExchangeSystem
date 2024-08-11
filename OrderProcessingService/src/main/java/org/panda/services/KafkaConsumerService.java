package org.panda.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;

@Service
@Slf4j
public class KafkaConsumerService {

    private final KafkaProducerService kafkaProducerService;

    public KafkaConsumerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }


    @KafkaListener(topics = "test", groupId = "match-making-processing-group")
    public void listenToTestTopic(String message) {
        processMessage(message, "test");
    }

    @KafkaListener(topics = "test1", groupId = "match-making-processing-group")
    public void listenToTest1Topic(@Payload String message,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        processMessage(message, "test1");
        log.info("Received message from partition: {}",partition);
    }

    private void processMessage(String message, String topic) {
        log.info("Received message: {} from topic {}", message, topic);
        kafkaProducerService.sendMessage("post_order_processing","rajan panda order is processed!!!!!");
    }
}

