package com.conexa.backend.scheduling.infrastructure.startup;


import com.conexa.backend.scheduling.domain.enums.UserRole;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AdminCreator {
    private final DoctorRepository doctorRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminIfNotExists() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (!doctorRepository.existsByRole(UserRole.ROLE_ADMIN)) {
            Doctor admin = new Doctor();
            admin.setEmail("admin@admin.com");
            admin.setCpf("725.646.450-97");
            admin.setTelephone("1234567890");
            admin.setSpecialty("General Medicine");
            admin.setPassword(new BCryptPasswordEncoder().encode("password1@"));
            admin.setBirthDate(LocalDate.parse("01/01/1990", formatter));
            admin.setRole(UserRole.ROLE_ADMIN);
            doctorRepository.save(admin);
            System.out.println("Admin user created.");
        }
    }
}

