package org.panda.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostOrderProcessingListener {

    @KafkaListener(topics = "post_order_processing", groupId = "post-processing-group")
    public void listen(String message) {
        // Save to MongoDB and update time series data
        log.info("Received processed order for MongoDB saving logic,Time series update logic,Notification logic: {}", message);
        // MongoDB saving logic
        // Time series update logic
        // Notification logic
    }
}
