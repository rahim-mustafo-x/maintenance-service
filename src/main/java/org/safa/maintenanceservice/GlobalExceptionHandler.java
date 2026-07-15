package org.safa.maintenanceservice;

import io.jsonwebtoken.ExpiredJwtException;
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ResponseBody<?>> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new  ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody<?>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new  ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseBody<?>> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseBody<>(HttpStatus.UNAUTHORIZED.value(), null, e.getMessage()));
    }
}
