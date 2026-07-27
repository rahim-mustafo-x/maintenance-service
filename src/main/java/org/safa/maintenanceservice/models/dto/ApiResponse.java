package org.safa.maintenanceservice.models.dto;

import lombok.*;

@Getter
@ToString
@Builder
public class ApiResponse<T> {
    private final int code;
    private T data;
    private String message;
}