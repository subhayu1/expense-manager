package com.rimalholdings.expensemanager.data.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String clientId;
    private String clientSecret;
}
