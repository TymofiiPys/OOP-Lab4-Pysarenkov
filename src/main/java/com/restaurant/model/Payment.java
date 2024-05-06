package com.restaurant.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@ToString
public class Payment {
    private int id;
    private int clientId;
    private Timestamp time;
    private double cost;
}
