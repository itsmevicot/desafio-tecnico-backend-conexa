package com.conexa.backend.scheduling.infrastructure.repositories;



import com.conexa.backend.scheduling.domain.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
    boolean existsByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
    boolean existsByPatientIdAndDateTimeBetween(Long patientId, LocalDateTime start, LocalDateTime end);
}

