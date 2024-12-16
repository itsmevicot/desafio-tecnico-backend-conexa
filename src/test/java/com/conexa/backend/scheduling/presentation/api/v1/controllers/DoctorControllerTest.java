package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.domain.exceptions.UnauthorizedAccessException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.UpdateDoctorRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    private Doctor testDoctor;
    private UpdateDoctorRequestDTO updateRequest;

    @BeforeEach
    void setup() {
        testDoctor = Doctor.builder()
                .id(1L)
                .email("test@example.com")
                .specialty("Cardiology")
                .birthDate(LocalDate.of(2000, 1, 1))
                .telephone("5511999999999")
                .build();

        updateRequest = new UpdateDoctorRequestDTO(
                "Neurology",
                LocalDate.of(1990, 5, 15),
                "5511987654321"
        );

        Mockito.when(doctorService.getDoctorByIdIfAuthorized(1L, "test@example.com"))
                .thenReturn(testDoctor);
        Mockito.when(doctorService.findByEmailIfAuthorized("test@example.com", "test@example.com"))
                .thenReturn(testDoctor);
        Mockito.when(doctorService.updateDoctorIfAuthorized(eq(1L), eq("test@example.com"), any()))
                .thenReturn(testDoctor);
        
        Mockito.doThrow(new UnauthorizedAccessException())
                .when(doctorService).getDoctorByIdIfAuthorized(1L, "unauthorized@example.com");
        Mockito.doThrow(new UnauthorizedAccessException())
                .when(doctorService).findByEmailIfAuthorized("test@example.com", "unauthorized@example.com");
        Mockito.doThrow(new UnauthorizedAccessException())
                .when(doctorService).updateDoctorIfAuthorized(eq(1L), eq("unauthorized@example.com"), any());

    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getDoctorById_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/doctor/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "unauthorized@example.com")
    void getDoctorById_shouldReturn403_whenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/doctor/{id}", 1L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void findByEmail_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/doctor/email").param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "unauthorized@example.com")
    void findByEmail_shouldReturn403_whenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/doctor/email").param("email", "test@example.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateDoctorById_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/doctor/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "unauthorized@example.com")
    void updateDoctorById_shouldReturn403_whenUnauthorized() throws Exception {
        mockMvc.perform(put("/api/v1/doctor/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .principal(() -> "unauthorized@example.com"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Unauthorized Access"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateDoctorById_shouldReturn400_whenInvalidInput() throws Exception {
        UpdateDoctorRequestDTO invalidRequest = new UpdateDoctorRequestDTO(
                "Neurology",
                LocalDate.of(2025, 1, 1), // Future date
                "invalid-phone"
        );

        mockMvc.perform(put("/api/v1/doctor/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
