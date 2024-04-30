package com.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class Order {
    private int id;
    private int clientId;
    private int menuId;
    private int amount;
    private StatusOrder status;

    public enum StatusOrder {ORDERED, ISSUED_FOR_PAYMENT, PAID}
}
