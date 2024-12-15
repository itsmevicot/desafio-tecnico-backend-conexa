package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.infrastructure.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .filter(Patient::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found or inactive"));
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = getPatientById(id);
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setCpf(updatedPatient.getCpf());
        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patient.setActive(false);
        patientRepository.save(patient);
    }
}
