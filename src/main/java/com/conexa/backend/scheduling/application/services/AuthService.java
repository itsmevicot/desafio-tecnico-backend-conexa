package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorAlreadyExistsException;
import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.domain.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registers a new doctor.
     *
     * @param doctor the doctor to be registered
     * @return the registered doctor
     */
    public Doctor signup(Doctor doctor) {
        if (doctorRepository.existsByCpf(doctor.getCpf())) {
            throw new DoctorAlreadyExistsException("A doctor with this CPF already exists.");
        }

        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DoctorAlreadyExistsException("A doctor with this email already exists.");
        }

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

        return doctorRepository.save(doctor);
    }

    /**
     * Authenticates a doctor and generates a JWT token.
     *
     * @param email    the email of the doctor
     * @param password the password of the doctor
     * @return the JWT token
     */
    public String login(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Invalid email or password."));

        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return jwtTokenProvider.generateToken(doctor);
    }

    /**
     * Logs out a doctor by invalidating the JWT token.
     *
     * @param token the JWT token to invalidate
     */
    public void logout(String token) {
        jwtTokenProvider.invalidateToken(token);
    }
}
