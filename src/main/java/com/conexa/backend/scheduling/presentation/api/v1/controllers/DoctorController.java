package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.UpdateDoctorRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.DoctorResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.DoctorMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctor")
@RestController
@RequiredArgsConstructor
public class DoctorController extends BaseV1Controller {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    /**
     * Retrieves the authenticated doctor's info by ID.
     *
     * @param id the ID of the doctor
     * @return the doctor with the given ID
     */
    @GetMapping("doctor/{id}")
    public DoctorResponseDTO getDoctorById(@PathVariable Long id) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorMapper.toResponseDTO(doctorService.getDoctorByIdIfAuthorized(id, authenticatedEmail));
    }

    /**
     * Retrieves the authenticated doctor's info by email.
     *
     * @param email the email of the doctor
     * @return the doctor with the given email
     */
    @GetMapping("doctor/email")
    public DoctorResponseDTO findByEmail(@RequestParam String email) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorMapper.toResponseDTO(doctorService.findByEmailIfAuthorized(email, authenticatedEmail));
    }

    @PutMapping("doctor/{id}")
    public DoctorResponseDTO updateDoctorById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequestDTO request) {
        String authenticatedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorMapper.toResponseDTO(doctorService.updateDoctorIfAuthorized(id, authenticatedEmail, request));
    }
}
