package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.exceptions.patient.PatientNotFoundException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.ConflictingScheduleException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.InvalidScheduleDateException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.domain.models.Schedule;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.repositories.PatientRepository;
import com.conexa.backend.scheduling.infrastructure.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public Schedule createSchedule(Schedule schedule) {
        Doctor doctor = getDoctor(schedule.getDoctor().getId());
        Patient patient = getPatient(schedule.getPatient().getId());

        validateScheduleDate(schedule.getDateTime());

        checkConflicts(doctor.getId(), patient.getId(), schedule.getDateTime());

        return scheduleRepository.save(schedule);
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("The doctor information could not be found."));
    }

    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(PatientNotFoundException::new);
    }

    private void validateScheduleDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidScheduleDateException();
        }
    }

    private void checkConflicts(Long doctorId, Long patientId, LocalDateTime dateTime) {
        boolean hasDoctorConflict = scheduleRepository.existsByDoctorIdAndDateTimeBetween(
                doctorId,
                dateTime.minusMinutes(20),
                dateTime.plusMinutes(20)
        );

        if (hasDoctorConflict) {
            throw new ConflictingScheduleException("The doctor has a conflicting schedule in a 20-minute interval.");
        }

        boolean hasPatientConflict = scheduleRepository.existsByPatientIdAndDateTimeBetween(
                patientId,
                dateTime.minusMinutes(20),
                dateTime.plusMinutes(20)
        );

        if (hasPatientConflict) {
            throw new ConflictingScheduleException("The patient has a conflicting schedule in a 20-minute interval.");
        }
    }
}
