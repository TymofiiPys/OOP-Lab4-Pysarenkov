package com.restaurant.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private int clientId;
    private double cost;
}
