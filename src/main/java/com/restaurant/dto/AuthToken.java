package com.restaurant.dto;

import lombok.Data;

@Data
public class AuthToken {
    private final int status;
    private final String jwt;
}
