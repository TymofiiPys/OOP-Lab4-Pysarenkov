package com.example.model;



public class Order {

    private int id;


    private int clientId;

    private int menuId;

    private int amount;

    public enum StatusOrder {ORDERED, ISSUED_FOR_PAYMENT, PAID};

    private StatusOrder status;

    public Order() {
    }

    public Order(int clientId, int menuId, int amount, StatusOrder status) {
        this.clientId = clientId;
        this.menuId = menuId;
        this.amount = amount;
        this.status = status;
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

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
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
