package org.safa.maintenanceservice.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ResponseBody <T> {
    private final int code;
    private T data;
    private String message;

    public ResponseBody(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseBody(int code, String message) {
        this.code = code;
        this.message = message;
    }

}