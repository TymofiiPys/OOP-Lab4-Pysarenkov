package com.restaurant.dto;

import lombok.Data;

@Data
public class OrderReceiveDTO {
    private int clientId;
    private int menuId;
    private int amount;
}
