package com.restaurant.dto;

import com.restaurant.model.Order;
import lombok.Data;

@Data
public class OrderDTO {
    private int id;
    private int clientId;
    private int menuId;
    private int amount;
    private Order.StatusOrder status;
}
