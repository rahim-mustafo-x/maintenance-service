package org.safa.maintenanceservice.labor.controller.labor;

import org.safa.maintenanceservice.ApiResponse;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborCreateRequest;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <pre>
 *   /v1/labor
 *  │
 *  ├── POST   /                         → create labor
 *  ├── GET    /me                       → my labor
 *  ├── GET    /search                   → search labors
 *  ├── GET    /{id}                     → public labor
 *  │
 *  ├── POST   /me/types                 → add type
 *  ├── DELETE /me/types/{type}          → remove type
 *  │
 *  ├── GET    /me/working-hours
 *  ├── PUT    /me/working-hours/{id}
 *  ├── DELETE /me/working-hours/{id}
 *  │
 *  └── PATCH  /me/availability
 *  └── PUT /v1/labor/me/image → to upload the image of labor for the profile
 * </pre>
 */

@RequestMapping("/v1/labor")
public interface LaborApi {
    //creates the profile
    @PostMapping
    ResponseEntity<ApiResponse<?>> setWorkingHours(@RequestBody LaborCreateRequest laborCreateRequest);
    @GetMapping("/me")
    ResponseEntity<ApiResponse<?>> laborMe();
    @GetMapping("/search")
    ResponseEntity<ApiResponse<?>> searchLabors(@RequestParam LaborType type, @RequestParam int page, @RequestParam int size);
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<?>> laborById(@PathVariable long id);

}
