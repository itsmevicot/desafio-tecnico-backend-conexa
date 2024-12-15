package com.conexa.backend.scheduling.presentation.api.v1.controllers;

import com.conexa.backend.scheduling.application.services.AuthService;
import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.BaseV1Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController extends BaseV1Controller {

    private final AuthService authService;

    /**
     * Registers a new doctor.
     *
     * @param doctor The doctor's details for registration.
     * @return The registered doctor.
     */
    @PostMapping("/signup")
    public ResponseEntity<Doctor> signup(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(authService.signup(doctor));
    }

    /**
     * Authenticates a doctor and generates a JWT token.
     *
     * @param email The doctor's email.
     * @param password The doctor's password.
     * @return A JWT token for authenticated access.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(email, password));
    }

    /**
     * Logs out a doctor by invalidating the JWT token.
     *
     * @param token The JWT token to invalidate (passed in the Authorization header).
     * @return A success message indicating logout was successful.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}
