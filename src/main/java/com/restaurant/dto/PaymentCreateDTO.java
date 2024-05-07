package com.restaurant.dto;

import lombok.Data;

@Data
public class PaymentCreateDTO {
    private int clientId;
    private double cost;
}
