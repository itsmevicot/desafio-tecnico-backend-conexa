package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.PatientService;
import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.PatientRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.PatientResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.PatientMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private PatientMapper patientMapper;

    private Patient testPatient;
    private PatientRequestDTO validRequestDTO;

    @BeforeEach
    void setup() {
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("John Doe");
        testPatient.setCpf("123.456.789-09");
        testPatient.setActive(true);

        validRequestDTO = new PatientRequestDTO(
                "John Doe Updated",
                "123.456.789-09"
        );

        Mockito.when(patientMapper.toResponseDTO(any()))
                .thenReturn(new PatientResponseDTO(1L, "John Doe", "123.456.789-09", true));
        Mockito.when(patientMapper.toEntity(any())).thenReturn(testPatient);
        Mockito.when(patientService.createPatient(any())).thenReturn(testPatient);
        Mockito.when(patientService.getPatientById(1L)).thenReturn(testPatient);
        Mockito.when(patientService.updatePatient(eq(1L), any())).thenReturn(testPatient);
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void createPatient_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(post("/api/v1/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getPatientById_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/patient/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePatient_shouldReturn200_whenAuthorized() throws Exception {
        mockMvc.perform(put("/api/v1/patient/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void updatePatient_shouldReturn403_whenUnauthorizedRole() throws Exception {
        mockMvc.perform(put("/api/v1/patient/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePatient_shouldReturn400_whenInvalidCPF() throws Exception {
        PatientRequestDTO invalidRequestDTO = new PatientRequestDTO(
                "John Doe",
                "invalid-cpf" // Invalid CPF
        );

        mockMvc.perform(put("/api/v1/patient/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePatient_shouldReturn204_whenAuthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/patient/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void deletePatient_shouldReturn403_whenUnauthorizedRole() throws Exception {
        mockMvc.perform(delete("/api/v1/patient/{id}", 1L))
                .andExpect(status().isForbidden());
    }
}
