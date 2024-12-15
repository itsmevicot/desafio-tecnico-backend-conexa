package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.PatientService;
import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patient")
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController extends BaseV1Controller {

    private final PatientService patientService;

    /**
     * Creates a new patient.
     *
     * @param patient the patient information
     * @return the created patient
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(createdPatient);
    }

    /**
     * Retrieves a patient by ID.
     *
     * @param id the ID of the patient
     * @return the patient with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    /**
     * Updates an existing patient.
     *
     * @param id the ID of the patient to update
     * @param updatedPatient the updated patient information
     * @return the updated patient
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Patient patient = patientService.updatePatient(id, updatedPatient);
        return ResponseEntity.ok(patient);
    }

    /**
     * Soft deletes a patient by marking them as inactive.
     *
     * @param id the ID of the patient to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient successfully deleted (soft delete)."); // TODO: Remove comment and return http status 204
    }
}
