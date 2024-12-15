package com.conexa.backend.scheduling.presentation.api.v1.dtos.responses;

import java.time.LocalDateTime;

public record ScheduleResponseDTO(
        Long id,
        LocalDateTime dateTime,
        String patientName
) {}
