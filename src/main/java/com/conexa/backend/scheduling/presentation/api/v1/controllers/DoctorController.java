package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.application.services.DoctorService;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctor")
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController extends BaseV1Controller {

    private final DoctorService doctorService;

    /**
     * Retrieves a doctor by ID.
     *
     * @param id the ID of the doctor
     * @return the doctor with the given ID
     */
    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    /**
     * Retrieves a doctor by email.
     *
     * @param email the email of the doctor
     * @return the doctor with the given email
     */
    @GetMapping("/email")
    public Doctor findByEmail(@RequestParam String email) {
        return doctorService.findByEmail(email);
    }
}
