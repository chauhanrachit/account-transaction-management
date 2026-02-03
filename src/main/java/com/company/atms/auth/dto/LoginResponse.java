package com.company.atms.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String username;
    private String role;
    private String message;
}
