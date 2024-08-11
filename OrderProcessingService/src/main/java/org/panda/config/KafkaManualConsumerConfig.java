//package org.panda.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.TopicPartitionOffset;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaManualConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "match-making-processing-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public KafkaMessageListenerContainer<String, String> containerForPartition0() {
//        return createContainer("test1", 0);
//    }
//
//    @Bean
//    public KafkaMessageListenerContainer<String, String> containerForPartition1() {
//        return createContainer("test1", 1);
//    }
//
//    @Bean
//    public KafkaMessageListenerContainer<String, String> containerForPartition2() {
//        return createContainer("test1", 2);
//    }
//
//    @Bean
//    public KafkaMessageListenerContainer<String, String> containerForPartition3() {
//        return createContainer("test1", 3);
//    }
//
//
//    private KafkaMessageListenerContainer<String, String> createContainer(String topic, int partition) {
//        return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties(topic, partition));
//    }
//
//    private ContainerProperties containerProperties(String topic, int partition) {
//        ContainerProperties properties = new ContainerProperties(new TopicPartitionOffset(topic, partition));
//        properties.setAckMode(ContainerProperties.AckMode.RECORD);
//        properties.setMessageListener(new MessageListener<String, String>() {
//            @Override
//            public void onMessage(org.apache.kafka.clients.consumer.ConsumerRecord<String, String> record) {
//                // Process the message and log partition info
//                System.out.printf("Received message: %s from topic: %s partition: %d%n",
//                        record.value(), record.topic(), record.partition());
//            }
//        });
//        return properties;
//    }
//}
