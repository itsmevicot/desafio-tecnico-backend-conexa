package com.conexa.backend.scheduling.presentation.api.v1.dtos;

public record PatientResponseDTO(
        Long id,
        String name,
        String cpf,
        boolean active
) {}
