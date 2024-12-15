package com.conexa.backend.scheduling.infrastructure.security.services;

import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DoctorDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    public DoctorDetailsService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with email: " + email));

        return User.builder()
                .username(doctor.getEmail())
                .password(doctor.getPassword())
                .roles("DOCTOR")
                .build();
    }
}
