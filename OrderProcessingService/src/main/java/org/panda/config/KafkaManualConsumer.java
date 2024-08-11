//package org.panda.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Properties;
//
//@Service
//public class KafkaManualConsumer {
//
//    private KafkaConsumer<String, String> consumer;
//
//    @PostConstruct
//    public void startConsumer() {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "match-making-processing-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList("test1"));
//
//        // Start a new thread to consume messages
//        new Thread(this::consumeMessages).start();
//    }
//
//    private void consumeMessages() {
//        while (true) {
//            consumer.poll(Duration.ofSeconds(1)).forEach(record -> {
//                // Process each record
//                System.out.printf("Consumed record with key %s and value %s from topic %s%n",
//                        record.key(), record.value(), record.topic());
//            });
//        }
//    }
//
//    @PreDestroy
//    public void stopConsumer() {
//        if (consumer != null) {
//            consumer.close();
//        }
//    }
//}
