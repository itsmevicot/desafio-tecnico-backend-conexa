package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.AuthService;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.DoctorResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.LoginRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.LoginResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.SignupRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.DoctorMapper;
import com.conexa.backend.scheduling.presentation.api.v1.validators.SignupRequestValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequiredArgsConstructor
public class AuthController extends BaseV1Controller {

    private final AuthService authService;
    private final DoctorMapper doctorMapper;

    @PostMapping("/signup")
    public ResponseEntity<DoctorResponseDTO> signup(@Valid @RequestBody SignupRequestDTO signupRequest) {
        SignupRequestValidator.validate(signupRequest);
        Doctor doctor = authService.signup(signupRequest);
        DoctorResponseDTO response = doctorMapper.toResponseDTO(doctor);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String jwtAccessToken = authService.login(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(new LoginResponseDTO(jwtAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logout successful.");
    }
}
