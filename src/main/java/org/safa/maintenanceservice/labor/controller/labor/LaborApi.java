package org.safa.maintenanceservice.labor.controller.labor;

import org.safa.maintenanceservice.ApiResponse;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborCreateRequest;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/labor")
public interface LaborApi {
    @PostMapping("/working-hours/labor-type")
    ResponseEntity<ApiResponse<?>> setWorkingHours(@RequestBody LaborCreateRequest laborCreateRequest);
    @GetMapping("/me")
    ResponseEntity<ApiResponse<?>> laborMe();
    @GetMapping("/search")
    ResponseEntity<ApiResponse<?>> searchLabors(@RequestParam LaborType type, @RequestParam int page, @RequestParam int size);
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<?>> laborById(@PathVariable long id);

}
