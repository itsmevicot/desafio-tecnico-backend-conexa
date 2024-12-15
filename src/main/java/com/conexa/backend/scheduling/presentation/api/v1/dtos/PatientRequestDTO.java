package com.conexa.backend.scheduling.presentation.api.v1.dtos;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.NotBlank;

public record PatientRequestDTO(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "CPF is mandatory")
        @CPF(message = "Invalid CPF format")
        String cpf
) {}
