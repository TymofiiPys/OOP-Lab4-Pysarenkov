package com.restaurant.dto;

import com.restaurant.model.Menu;
import lombok.Data;

@Data
public class OrderShowToAdminDTO {
    private int id;
    private String clientName;
    private String menuItemName;
    private int amount;
}
