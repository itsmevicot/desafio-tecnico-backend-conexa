package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.enums.UserRole;
import com.conexa.backend.scheduling.domain.exceptions.auth.InvalidCredentialsException;
import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorAlreadyExistsException;
import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.security.jwt.JwtUtil;
import com.conexa.backend.scheduling.infrastructure.security.services.TokenBlacklistService;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.SignupRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public Doctor signup(SignupRequestDTO signupRequest) {
        log.info("Attempting to sign up a new doctor with email: {}, CPF: {}", signupRequest.email(), signupRequest.cpf());

        if (doctorRepository.existsByCpf(signupRequest.cpf())) {
            log.warn("Signup failed: A doctor with CPF {} already exists.", signupRequest.cpf());
            throw new DoctorAlreadyExistsException("A doctor with this CPF already exists.");
        }

        if (doctorRepository.existsByEmail(signupRequest.email())) {
            log.warn("Signup failed: A doctor with email {} already exists.", signupRequest.email());
            throw new DoctorAlreadyExistsException("A doctor with this email already exists.");
        }

        Doctor doctor = AuthMapper.toDoctor(signupRequest);
        log.debug("Mapping SignupRequestDTO to Doctor: {}", doctor);

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        log.debug("Encoded password for doctor: {}", doctor.getEmail());

        doctor.setRole(UserRole.ROLE_DOCTOR);
        log.debug("Set role for doctor: {}", doctor.getEmail());

        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Signup successful for doctor with email: {}", savedDoctor.getEmail());

        return savedDoctor;
    }

    public String login(String email, String password) {
        log.info("Attempting login for email: {}", email);

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed: No doctor found with email: {}", email);
                    return new InvalidCredentialsException();
                });

        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            log.warn("Login failed: Password mismatch for email: {}", email);
            throw new InvalidCredentialsException();
        }

        String role = doctor.getRole().name();
        List<String> roles = List.of(role);

        String token = jwtUtil.generateToken(email, roles);
        log.info("Login successful for email: {}. JWT token generated: {}", email, token);

        return token;
    }

    public void logout(String token) {
        log.info("Attempting logout for token: {}", token);

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
            log.debug("Stripped 'Bearer ' prefix. Token: {}", token);
        }

        Date expirationDate = jwtUtil.extractExpiration(token);
        log.debug("Extracted expiration date from token: {}", expirationDate);

        Duration duration = Duration.between(Instant.now(), expirationDate.toInstant());
        log.debug("Calculated token expiration duration: {}", duration);

        tokenBlacklistService.invalidateToken(token, duration);
        log.info("Token successfully blacklisted: {}", token);
    }
}
