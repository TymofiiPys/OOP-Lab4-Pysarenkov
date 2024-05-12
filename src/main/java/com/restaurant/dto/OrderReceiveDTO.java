package com.restaurant.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrderReceiveDTO {
//    private int menuId;
//    private int amount;
    Map<String, Integer> menuAmts;
}
