package org.panda.controller;


import org.panda.services.KafkaProducerService;
import org.panda.services.KafkaTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;
    private final KafkaTopicService kafkaTopicService;

    @Autowired
    public KafkaController(KafkaProducerService kafkaProducerService, KafkaTopicService kafkaTopicService) {
        this.kafkaProducerService = kafkaProducerService;
        this.kafkaTopicService = kafkaTopicService;
    }

    @PostMapping("/createTopic")
    public String createTopic(@RequestParam String topicName,
                              @RequestParam int partitions,
                              @RequestParam short replicationFactor) {
        return kafkaTopicService.createTopic(topicName, partitions, replicationFactor);
    }

    @GetMapping("/topicPartitions")
    public Map<String, Integer> getTopicPartitions() throws ExecutionException, InterruptedException {
        return kafkaTopicService.getTopicPartitions();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestParam String topic, @RequestParam String message) {
        kafkaProducerService.sendMessage(topic, message);
    }
}
