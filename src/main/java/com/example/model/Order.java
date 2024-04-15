package com.example.model;



public class Order {

    private int id;


    private int clientId;

    private boolean menuId;

    private int amount;

    public enum StatusOrder {ORDERED, CONSUMED, PAID};

    private StatusOrder status;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public boolean isMenuId() {
        return menuId;
    }

    public void setMenuId(boolean menuId) {
        this.menuId = menuId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder status) {
        this.status = status;
    }
}
