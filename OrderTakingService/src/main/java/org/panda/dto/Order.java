package org.panda.dto;

import lombok.Data;

@Data
public class Order {
    private String type; // "buy" or "sell"
    private double price;
    private int quantity;
}
