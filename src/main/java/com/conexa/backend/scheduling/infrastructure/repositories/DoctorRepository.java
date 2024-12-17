package com.conexa.backend.scheduling.infrastructure.repositories;


import com.conexa.backend.scheduling.domain.enums.UserRole;
import com.conexa.backend.scheduling.domain.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByRole(UserRole role);
}
