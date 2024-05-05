package com.restaurant.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Client {
    private int id;
    private String name;
}
