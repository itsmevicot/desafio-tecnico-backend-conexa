package com.conexa.backend.scheduling.application.services;

import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorAlreadyExistsException;
import com.conexa.backend.scheduling.domain.exceptions.doctor.DoctorNotFoundException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.domain.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.security.jwt.JwtUtil;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.SignupRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Doctor signup(SignupRequestDTO signupRequest) {
        if (doctorRepository.existsByCpf(signupRequest.cpf())) {
            throw new DoctorAlreadyExistsException("A doctor with this CPF already exists.");
        }

        if (doctorRepository.existsByEmail(signupRequest.email())) {
            throw new DoctorAlreadyExistsException("A doctor with this email already exists.");
        }

        Doctor doctor = AuthMapper.toDoctor(signupRequest);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

        return doctorRepository.save(doctor);
    }

    public String login(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Invalid email or password."));

        if (!passwordEncoder.matches(password, doctor.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return jwtUtil.generateToken(email);
    }

    public void logout(String token) {
        jwtUtil.invalidateToken(token);
    }
}
