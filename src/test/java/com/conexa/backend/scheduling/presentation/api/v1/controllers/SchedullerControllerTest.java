package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.application.services.ScheduleService;
import com.conexa.backend.scheduling.domain.exceptions.schedule.ConflictingScheduleException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.DoctorSelfScheduleException;
import com.conexa.backend.scheduling.domain.exceptions.schedule.InvalidScheduleDateException;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.PatientDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.ScheduleResponseDTO;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private ScheduleService scheduleService;

    private Doctor testDoctor;
    private CreateScheduleRequestDTO validRequest;

    @BeforeEach
    void setup() {
        testDoctor = Doctor.builder()
                .id(1L)
                .email("test@example.com")
                .cpf("123.456.789-09")
                .build();

        validRequest = new CreateScheduleRequestDTO(
                LocalDateTime.now().plusDays(1),
                new PatientDTO("John Doe", "987.654.321-00")
        );

        Mockito.when(doctorService.findByEmail("test@example.com")).thenReturn(testDoctor);

        Mockito.when(scheduleService.createSchedule(any(CreateScheduleRequestDTO.class), eq(1L)))
                .thenReturn(new ScheduleResponseDTO(
                        1L,
                        validRequest.dateTime(),
                        "John Doe"
                ));

    }

    @Test
    @WithMockUser(username = "test@example.com")
    void createSchedule_shouldReturn200_whenValidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName").value("John Doe"))
                .andExpect(jsonPath("$.dateTime").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void createSchedule_shouldReturn400_whenScheduleDateIsPast() throws Exception {
        CreateScheduleRequestDTO invalidRequest = new CreateScheduleRequestDTO(
                LocalDateTime.now().minusDays(1),
                new PatientDTO("John Doe", "987.654.321-00")
        );

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.message.dateTime").value("The schedule date and time must be in the future"));
    }



    @Test
    @WithMockUser(username = "test@example.com")
    void createSchedule_shouldReturn400_whenDoctorSchedulesSelf() throws Exception {
        CreateScheduleRequestDTO invalidRequest = new CreateScheduleRequestDTO(
                LocalDateTime.now().plusDays(1),
                new PatientDTO("Dr. John", "123.456.789-09")
        );

        Mockito.doThrow(new DoctorSelfScheduleException()).when(scheduleService)
                .createSchedule(any(CreateScheduleRequestDTO.class), eq(1L));

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Doctor Self-Schedule"))
                .andExpect(jsonPath("$.message").value("Doctors cannot schedule appointments with themselves."));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void createSchedule_shouldReturn400_whenConflictingScheduleExists() throws Exception {
        Mockito.doThrow(new ConflictingScheduleException("The doctor has a conflicting schedule in a 20-minute interval."))
                .when(scheduleService).createSchedule(any(CreateScheduleRequestDTO.class), eq(1L));

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Schedule Conflict"))
                .andExpect(jsonPath("$.message").value("The doctor has a conflicting schedule in a 20-minute interval."));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void createSchedule_shouldReturn400_whenInvalidPatientData() throws Exception {
        CreateScheduleRequestDTO invalidRequest = new CreateScheduleRequestDTO(
                LocalDateTime.now().plusDays(1),
                new PatientDTO("", "")
        );

        mockMvc.perform(post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
