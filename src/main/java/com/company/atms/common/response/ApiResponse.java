package com.company.atms.common.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private Instant timestamp;
    private int status;
    private String message;
    private T data;
    private String path;
}