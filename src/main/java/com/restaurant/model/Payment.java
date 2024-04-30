package com.restaurant.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class Payment {
    private int id;
    private int clientId;
    private Timestamp time;
    private double cost;
}
