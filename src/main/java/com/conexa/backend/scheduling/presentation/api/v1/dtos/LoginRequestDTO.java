package com.conexa.backend.scheduling.presentation.api.v1.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password
) {}
