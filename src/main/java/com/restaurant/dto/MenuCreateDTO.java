package com.restaurant.dto;

import lombok.Data;

@Data
public class MenuCreateDTO {
    private String name;
    private boolean mealOrDrink;
    private double cost;
}
