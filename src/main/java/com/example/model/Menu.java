package com.example.model;


public class Menu {

    private int id;

    private String name;

    /**
     * Represents whether the item is a meal or a drink, where
     * {@code true} means it is a meal, and {@code false} - a drink.
     */
    private boolean mealOrDrink;

    private double cost;

    public Menu(){}

    public Menu(int id, String name, boolean mealOrDrink, double cost) {
        this.id = id;
        this.name = name;
        this.mealOrDrink = mealOrDrink;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMealOrDrink() {
        return mealOrDrink;
    }

    public void setMealOrDrink(boolean mealOrDrink) {
        this.mealOrDrink = mealOrDrink;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
