package com.conexa.backend.scheduling.infrastructure.repositories;

import com.conexa.backend.scheduling.domain.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCpf(String cpf);
}
