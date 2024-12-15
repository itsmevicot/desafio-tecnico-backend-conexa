package com.conexa.backend.scheduling.presentation.api.v1.dtos;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.NotBlank;

public record PatientDTO(

        @NotBlank(message = "Patient name is mandatory")
        String name,

        @NotBlank(message = "CPF is mandatory")
        @CPF(message = "Invalid CPF format")
        String cpf
) {

        public PatientDTO {
                name = normalizeName(name);
                cpf = normalizeCPF(cpf);
        }

        private static String normalizeName(String name) {
                return name == null ? null : name.trim();
        }

        private static String normalizeCPF(String cpf) {
                return cpf == null ? null : cpf.replaceAll("\\D", "");
        }
}
