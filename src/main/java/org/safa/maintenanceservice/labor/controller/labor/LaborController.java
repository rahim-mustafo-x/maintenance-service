package org.safa.maintenanceservice.labor.controller.labor;

import lombok.RequiredArgsConstructor;
import org.safa.maintenanceservice.ApiResponse;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborCreateRequest;
import org.safa.maintenanceservice.admin.exceptions.BadRequestException;
import org.safa.maintenanceservice.admin.exceptions.NotFoundException;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborResponse;
import org.safa.maintenanceservice.labor.model.dto.labor.SearchLaborResponse;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import org.safa.maintenanceservice.labor.service.LaborService;
import org.safa.maintenanceservice.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class LaborController implements LaborApi {
    private final LaborService laborService;
    private final UserService userService;
    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = Objects.requireNonNull(authentication).getName();
        return userService.findUserIdByUserName(username);
    }

    @Override
    public ResponseEntity<ApiResponse<?>>  setWorkingHours(@RequestBody LaborCreateRequest laborCreateRequest){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.ACCEPTED.value())
                            .data(laborService.saveWorkingHours(laborCreateRequest, getCurrentUserId()))
                            .build());
        }catch (NullPointerException | BadRequestException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_ACCEPTABLE.value())
                            .message(e.getMessage())
                            .build());
        } catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> laborMe() {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.FOUND.value())
                            .data(laborService.laborById(getCurrentUserId()))
                            .build());
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> searchLabors(LaborType type, int page, int size) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.<Page<SearchLaborResponse>>builder()
                            .code(HttpStatus.FOUND.value())
                            .data(laborService.searchLabors(type, page, size))
                            .build());
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<ApiResponse<?>> laborById(long id) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.<LaborResponse>builder()
                            .code(HttpStatus.FOUND.value())
                            .data(laborService.laborById(id))
                            .build());
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiResponse.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
