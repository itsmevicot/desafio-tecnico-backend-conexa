package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.application.services.ScheduleService;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.ScheduleResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.validators.CreateScheduleRequestValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleController extends BaseV1Controller {

    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody CreateScheduleRequestDTO request) {
        CreateScheduleRequestValidator.validate(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Doctor doctor = doctorService.findByEmail(email);

        ScheduleResponseDTO createdSchedule = scheduleService.createSchedule(request, doctor.getId());
        return ResponseEntity.ok(createdSchedule);
    }
}
