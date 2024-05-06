package com.restaurant.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Menu {
    private int id;
    private String name;
    /**
     * Represents whether the item is a meal or a drink, where
     * {@code true} means it is a meal, and {@code false} - a drink.
     */
    private boolean mealOrDrink;
    private double cost;
}
