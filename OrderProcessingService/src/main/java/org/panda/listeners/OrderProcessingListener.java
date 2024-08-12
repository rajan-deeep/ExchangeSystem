package org.panda.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.panda.listeners.dto.Order;
import org.panda.services.KafkaProducerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

@Service
@Slf4j
public class OrderProcessingListener {

    private static final int MAX_QUEUE_SIZE = 3;

    private final PriorityQueue<Order> buyOrders = new PriorityQueue<>((a, b) -> Double.compare(b.getPrice(), a.getPrice())); // Max heap
    private final PriorityQueue<Order> sellOrders = new PriorityQueue<>(); // Min heap

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProducerService kafkaProducerService;

    public OrderProcessingListener(KafkaTemplate<String, String> kafkaTemplate, KafkaProducerService kafkaProducerService) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }

    @KafkaListener(topics = "tata_buy", groupId = "match-making-processing-group")
    public void listenBuy(String message) {
        Order order = parseOrder(message);
        if (buyOrders.size() < MAX_QUEUE_SIZE) {
            buyOrders.offer(order);
            matchOrders();
        } else {
            log.info("Buy orders queue is full. Order not added.");
        }
    }

    @KafkaListener(topics = "tata_sell", groupId = "match-making-processing-group")
    public void listenSell(String message) {
        Order order = parseOrder(message);
        if (sellOrders.size() < MAX_QUEUE_SIZE) {
            sellOrders.offer(order);
            matchOrders();
        } else {
            log.info("Sell orders queue is full. Order not added.");
        }
    }

    private Order parseOrder(String message) {
        Order order = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            order = objectMapper.readValue(message, Order.class);
        } catch (Exception ex) {
            log.error("Error while parsing the Order");
        }
        return order;
    }

    private void matchOrders() {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order buyOrder = buyOrders.peek();
            Order sellOrder = sellOrders.peek();

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int matchedQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                buyOrder.reduceQuantity(matchedQuantity);
                sellOrder.reduceQuantity(matchedQuantity);

                log.info("Matched Buy Order: " + buyOrder.getPrice() + " with Sell Order: " + sellOrder.getPrice() + ", matched quantity: " + matchedQuantity);
                kafkaProducerService.sendMessage("post_order_processing","rajan panda buy-sell order is processed!!!!!");

                if (buyOrder.isFullyProcessed()) {
                    buyOrders.poll();
                } else {
                    // Push back the remaining buyOrder to the topic
                    kafkaTemplate.send("tata_buy", formatBuyOrder(buyOrder));
                    log.info("Order pushed back to topic tata_buy, q: {},p: {}", buyOrder.getQuantity(), buyOrder.getPrice());
                }

                if (sellOrder.isFullyProcessed()) {
                    sellOrders.poll();
                } else {
                    // Push back the remaining sellOrder to the topic
                    kafkaTemplate.send("tata_sell", formatBuyOrder(sellOrder));
                    log.info("Order pushed back to topic tata_sell, q: {},p: {}", sellOrder.getQuantity(), sellOrder.getPrice());
                }
            } else {
                log.info("No match found buy q: {},p: {}", buyOrder.getQuantity(), buyOrder.getPrice());
                log.info("No match found sell q: {},p: {}", sellOrder.getQuantity(), sellOrder.getPrice());
                break; // No match found
            }
        }
    }

    private String formatBuyOrder(Order order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(order);
        } catch (Exception ex){
            log.info("Error while parsing for pushing again into the queue");
        }
        return null;
    }
}
