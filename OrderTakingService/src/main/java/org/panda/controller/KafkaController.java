package org.panda.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.panda.dto.Order;
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

    @PostMapping("/orders")
    public void placeOrder(@RequestBody Order order) throws Exception {
        String topic = "tata_" + order.getType(); // "tata_buy" or "tata_sell"
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaProducerService.sendMessage(topic, objectMapper.writeValueAsString(order));
    }
}
