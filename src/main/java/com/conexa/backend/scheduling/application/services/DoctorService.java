package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.exceptions.UnauthorizedAccessException;
import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.UpdateDoctorRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with the provided id."));
    }

    public Doctor getDoctorByIdIfAuthorized(Long id, String authenticatedEmail) {
        Doctor doctor = getDoctorById(id);
        if (!doctor.getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedAccessException();
        }
        return doctor;
    }

    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with the provided email."));
    }

    public Doctor findByEmailIfAuthorized(String email, String authenticatedEmail) {
        if (!email.equals(authenticatedEmail)) {
            throw new UnauthorizedAccessException();
        }
        return findByEmail(email);
    }

    public Doctor updateDoctorIfAuthorized(Long id, String authenticatedEmail, UpdateDoctorRequestDTO request) {
        Doctor doctor = getDoctorById(id);

        if (!doctor.getEmail().equals(authenticatedEmail)) {
            throw new UnauthorizedAccessException();
        }

        doctorMapper.updateDoctorFromDTO(request, doctor);

        return doctorRepository.save(doctor);
    }
}
