package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.infrastructure.repositories.DoctorRepository;
import com.conexa.backend.scheduling.infrastructure.security.jwt.JwtUtil;
import com.conexa.backend.scheduling.infrastructure.security.services.TokenBlacklistService;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.SignupRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private DoctorRepository doctorRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtil jwtUtil;

    private Doctor testDoctor;

    @BeforeEach
    void setup() {
        testDoctor = new Doctor();
        testDoctor.setEmail("test@example.com");
        testDoctor.setPassword("encodedPassword");

        Mockito.when(passwordEncoder.encode("Password@123"))
                .thenReturn("encodedPassword");
        Mockito.when(passwordEncoder.matches("Password@123", "encodedPassword"))
                .thenReturn(true);

        Mockito.when(doctorRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testDoctor));
        Mockito.when(doctorRepository.save(Mockito.any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(jwtUtil.generateToken("test@example.com"))
                .thenReturn("mock.jwt.token");
        Mockito.when(jwtUtil.validateToken("valid.jwt.token"))
                .thenReturn(true);
        Mockito.when(jwtUtil.extractEmail("valid.jwt.token"))
                .thenReturn("test@example.com");
        Mockito.when(jwtUtil.extractExpiration("valid.jwt.token"))
                .thenReturn(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)));
        Mockito.when(tokenBlacklistService.isTokenBlacklisted("blacklisted.jwt.token"))
                .thenReturn(true);
        Mockito.when(tokenBlacklistService.isTokenBlacklisted("valid.jwt.token"))
                .thenReturn(false);
        Mockito.when(jwtUtil.validateToken("blacklisted.jwt.token"))
                .thenReturn(false);
        Mockito.when(jwtUtil.extractExpiration("blacklisted.jwt.token"))
                .thenReturn(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)));
    }

    @Test
    void signup_shouldReturn200_whenValidRequest() throws Exception {
        SignupRequestDTO request = new SignupRequestDTO(
                "test@example.com",
                "Password@123",
                "Password@123",
                "Cardiology",
                "123.456.789-09",
                "01/01/2000",
                "(11) 99999-9999"
        );

        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    void signup_shouldReturn400_whenPasswordsDoNotMatch() throws Exception {
        SignupRequestDTO request = new SignupRequestDTO(
                "test@example.com",
                "Password@123",
                "WrongPassword",
                "Cardiology",
                "123.456.789-09",
                "01/01/2000",
                "(11) 99999-9999"
        );

        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn200_whenValidCredentials() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(
                "test@example.com",
                "Password@123"
        );

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").value("mock.jwt.token"));
    }

    @Test
    void login_shouldReturn401_whenInvalidCredentials() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO(
                "test@example.com",
                "WrongPassword"
        );

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_shouldReturn200_whenTokenIsValid() throws Exception {
        String validToken = "Bearer valid.jwt.token";

        mockMvc.perform(post("/api/v1/logout")
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    void logout_shouldReturn401_whenTokenIsBlacklisted() throws Exception {
        String blacklistedToken = "Bearer blacklisted.jwt.token";

        mockMvc.perform(post("/api/v1/logout")
                        .header("Authorization", blacklistedToken))
                .andExpect(status().isUnauthorized());
    }
}
