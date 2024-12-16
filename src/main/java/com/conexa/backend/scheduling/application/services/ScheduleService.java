package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.ConflictingScheduleException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.InvalidScheduleDateException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.domain.models.Schedule;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.repositories.PatientRepository;
import com.conexa.backend.scheduling.infrastructure.repositories.ScheduleRepository;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.PatientDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.ScheduleResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleResponseDTO createSchedule(CreateScheduleRequestDTO dto, Long doctorId) {
        Doctor doctor = getDoctor(doctorId);
        Patient patient = getOrCreatePatient(dto.patient());

        validateScheduleDate(dto.dateTime());
        checkConflicts(doctor.getId(), patient.getId(), dto.dateTime());

        Schedule schedule = scheduleMapper.toSchedule(dto, patient, doctor);
        schedule = scheduleRepository.save(schedule);

        return scheduleMapper.toResponseDTO(schedule);
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("The doctor information could not be found."));
    }

    private Patient getOrCreatePatient(PatientDTO patientDTO) {
        return patientRepository.findByCpf(patientDTO.cpf())
                .map(existingPatient -> updatePatientNameIfNecessary(existingPatient, patientDTO.name()))
                .orElseGet(() -> patientRepository.save(
                        Patient.builder()
                                .name(patientDTO.name())
                                .cpf(patientDTO.cpf())
                                .build()
                ));
    }

    private Patient updatePatientNameIfNecessary(Patient existingPatient, String newName) {
        if (!existingPatient.getName().equals(newName)) {
            existingPatient.setName(newName);
            return patientRepository.save(existingPatient);
        }
        return existingPatient;
    }

    private void validateScheduleDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidScheduleDateException();
        }
    }

    private void checkConflicts(Long doctorId, Long patientId, LocalDateTime dateTime) {
        boolean hasDoctorConflict = scheduleRepository.existsByDoctorIdAndDateTimeBetween(
                doctorId, dateTime.minusMinutes(20), dateTime.plusMinutes(20)
        );
        if (hasDoctorConflict) {
            throw new ConflictingScheduleException("The doctor has a conflicting schedule in a 20-minute interval.");
        }

        boolean hasPatientConflict = scheduleRepository.existsByPatientIdAndDateTimeBetween(
                patientId, dateTime.minusMinutes(20), dateTime.plusMinutes(20)
        );
        if (hasPatientConflict) {
            throw new ConflictingScheduleException("The patient has a conflicting schedule in a 20-minute interval.");
        }
    }
}
