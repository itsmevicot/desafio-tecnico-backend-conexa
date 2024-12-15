package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.ScheduleService;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.ScheduleResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule")
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController extends BaseV1Controller {

    private final ScheduleService scheduleService;

    /**
     * Creates a new schedule.
     *
     * @param request the schedule information
     * @return the created schedule response
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody CreateScheduleRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long doctorId = (Long) authentication.getPrincipal(); // Assuming principal returns the doctor ID.

        ScheduleResponseDTO createdSchedule = scheduleService.createSchedule(request, doctorId);
        return ResponseEntity.ok(createdSchedule);
    }
}
