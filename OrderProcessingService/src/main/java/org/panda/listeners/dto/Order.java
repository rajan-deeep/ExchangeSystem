package org.panda.listeners.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Comparable<Order> {
    private String type; // "buy" or "sell"
    private double price;
    private int quantity;

    public void reduceQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public boolean isFullyProcessed() {
        return this.quantity <= 0;
    }

    @Override
    public int compareTo(Order other) {
        // Assuming the comparison is primarily based on price
        // You can customize the logic here as per your requirement

        return Double.compare(this.price, other.price);
    }

}
