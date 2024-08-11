package org.panda.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaTopicService {

    private final AdminClient adminClient;

    @Autowired
    public KafkaTopicService(AdminClient adminClient) {
        this.adminClient = adminClient;
    }

    public String createTopic(String topicName, int partitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
        try {
            CreateTopicsResult result = adminClient.createTopics(Collections.singleton(newTopic));
            result.all().get(); // Wait for the topic creation to complete
            return "Topic created successfully";
        } catch (InterruptedException | ExecutionException e) {
            log.error("error while creating topic", e);
            return "Error creating topic: " + e.getMessage();
        }
    }

    public Map<String, Integer> getTopicPartitions() throws ExecutionException, InterruptedException {
        ListTopicsResult topics = adminClient.listTopics();
        Map<String, Integer> topicPartitions = new HashMap<>();
        for (String topic : topics.names().get()) {
            TopicDescription topicDescription = adminClient.describeTopics(Collections.singleton(topic)).all().get().get(topic);
            topicPartitions.put(topic, topicDescription.partitions().size());
        }
        return topicPartitions;
    }
}
