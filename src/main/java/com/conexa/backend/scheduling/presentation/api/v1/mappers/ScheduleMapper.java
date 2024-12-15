package com.conexa.backend.scheduling.presentation.api.v1.mappers;

import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.domain.models.Schedule;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.ScheduleResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {
    public Schedule toSchedule(CreateScheduleRequestDTO dto, Patient patient, Doctor doctor) {
        return Schedule.builder()
                .dateTime(dto.dateTime())
                .patient(patient)
                .doctor(doctor)
                .build();
    }

    public ScheduleResponseDTO toResponseDTO(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getDateTime(),
                schedule.getPatient().getName()
        );
    }
}
