package com.restaurant.dto;

import lombok.Data;

@Data
public class OrderDisplayDTO {
    private int id;
    private String clientName;
    private String menuItemName;
    private int amount;
    private double cost;
}
