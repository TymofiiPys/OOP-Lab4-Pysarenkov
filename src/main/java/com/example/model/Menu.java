package com.example.model;

import jakarta.persistence.*;


public class Menu {

    private int id;

    private String name;

    private boolean meal_drink;

    private double cost;

    public Menu(){}

    public Menu(int id, String name, boolean meal_drink, double cost) {
        this.id = id;
        this.name = name;
        this.meal_drink = meal_drink;
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

    public boolean isMeal_drink() {
        return meal_drink;
    }

    public void setMeal_drink(boolean meal_drink) {
        this.meal_drink = meal_drink;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
