package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.DoctorResponseDTO;
import com.conexa.backend.scheduling.presentation.api.v1.mappers.DoctorMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctor")
@RestController
@RequiredArgsConstructor
public class DoctorController extends BaseV1Controller {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    /**
     * Retrieves a doctor by ID.
     *
     * @param id the ID of the doctor
     * @return the doctor with the given ID
     */
    @GetMapping("doctor/{id}")
    public DoctorResponseDTO getDoctorById(@PathVariable Long id) {
        return doctorMapper.toResponseDTO(doctorService.getDoctorById(id));
    }

    /**
     * Retrieves a doctor by email.
     *
     * @param email the email of the doctor
     * @return the doctor with the given email
     */
    @GetMapping("doctor/email")
    public DoctorResponseDTO findByEmail(@RequestParam String email) {
        return doctorMapper.toResponseDTO(doctorService.findByEmail(email));
    }
}
