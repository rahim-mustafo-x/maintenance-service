package org.safa.maintenanceservice;

import org.safa.maintenanceservice.models.dto.ResponseBody;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<ResponseBody<?>> handleIllegalAccessError(IllegalAccessError e) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ResponseBody<?>> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new  ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseBody<?>> handleException(Exception e) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new  ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }
}
