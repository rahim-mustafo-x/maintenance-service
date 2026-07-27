package org.safa.maintenanceservice.controller;

import org.safa.maintenanceservice.models.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> root() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.<String>builder()
                        .code(HttpStatus.OK.value())
                        .data("API active")
                        .message("Welcome to Maintenance API. If this is on development you can see API documentation with /swagger-ui/index.html. Have fun!")
                        .build());
    }
}