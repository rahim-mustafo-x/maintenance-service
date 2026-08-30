package org.safa.maintenanceservice.labor.service;

import lombok.RequiredArgsConstructor;
import org.safa.maintenanceservice.admin.exceptions.BadRequestException;
import org.safa.maintenanceservice.admin.exceptions.NotFoundException;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborCreateRequest;
import org.safa.maintenanceservice.labor.model.dto.labor.LaborResponse;
import org.safa.maintenanceservice.labor.model.dto.labor.SearchLaborResponse;
import org.safa.maintenanceservice.labor.model.dto.workingHours.WorkingHourResponse;
import org.safa.maintenanceservice.labor.model.entity.LaborEntity;
import org.safa.maintenanceservice.labor.model.entity.WorkingHoursEntity;
import org.safa.maintenanceservice.labor.model.model.LaborType;
import org.safa.maintenanceservice.labor.repository.LaborRepository;
import org.safa.maintenanceservice.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaborServiceImpl implements LaborService {
    private final LaborRepository laborRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public boolean saveWorkingHours(LaborCreateRequest laborCreateRequest, long userId) {
        if (laborCreateRequest.workingHoursRequests().isEmpty()) {
            throw new BadRequestException("No working hours requests were provided");
        }
        var laborEntity = laborRepository.findByUserId(userId)
                .orElseGet(()->{
                    var user = userRepository.findById(userId)
                            .orElseThrow(() -> new NotFoundException("User not found"));
                    return LaborEntity.builder()
                            .laborTypes(laborCreateRequest.laborTypes())
                            .user(user)
                            .build();
                });
        Set<WorkingHoursEntity> workingHoursEntities = laborCreateRequest.workingHoursRequests().stream().map(item -> {
                    if (item.startTime().isAfter(item.endTime())) {
                        throw new BadRequestException("Start time cannot be after end time");
                    }
                    return WorkingHoursEntity.builder()
                            .laborEntity(laborEntity)
                            .day(item.day())
                            .startTime(item.startTime())
                            .endTime(item.endTime())
                            .build();
                }
        ).collect(Collectors.toSet());
        laborEntity.getWorkingHours().clear();
        laborEntity.getWorkingHours().addAll(workingHoursEntities);
        laborRepository.save(laborEntity);
        return true;
    }

    @Override
    public LaborResponse laborById(long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        var laborEntity = laborRepository.findByUserId(userId).orElseThrow(()->new NotFoundException("User not found"));
        var workingHours = laborEntity.getWorkingHours().stream().map(
                item-> WorkingHourResponse.builder()
                        .id(item.getId())
                        .day(item.getDay())
                        .startTime(item.getStartTime())
                        .endTime(item.getEndTime())
                        .build()
        ).collect(Collectors.toSet());
        return LaborResponse.builder()
                .id(laborEntity.getId())
                .userId(laborEntity.getUser().getId())
                .fullName(user.getFullName())
                .laborTypes(laborEntity.getLaborTypes())
                .workingHours(workingHours)
                .build();
    }

    @Override
    public Page<SearchLaborResponse> searchLabors(LaborType type, int page, int size) {
        return laborRepository.findAllByType(type, PageRequest.of(page, size)).map(item->new SearchLaborResponse(item.getId(), item.getLaborTypes(), item.getUser().getFullName()));
    }
}
