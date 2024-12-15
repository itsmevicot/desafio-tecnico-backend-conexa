package com.conexa.backend.scheduling.presentation.api.v1.dtos.requests;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequestDTO(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password,

        @NotBlank(message = "Confirm password is mandatory")
        String confirmPassword,

        @NotBlank(message = "Specialty is mandatory")
        String specialty,

        @NotBlank(message = "CPF is mandatory")
        @CPF(message = "Invalid CPF format")
        String cpf,

        @NotBlank(message = "Birth date is mandatory")
        String birthDate,

        @NotBlank(message = "Telephone is mandatory")
        String telephone
) {

        public SignupRequestDTO {
                email = normalizeEmail(email);
                password = password.trim();
                confirmPassword = confirmPassword.trim();
                specialty = specialty.trim();
                cpf = normalizeCPF(cpf);
                birthDate = birthDate.trim();
                telephone = normalizePhone(telephone);
        }

        private static String normalizeEmail(String email) {
                return email == null ? null : email.trim().toLowerCase();
        }

        private static String normalizeCPF(String cpf) {
                return cpf == null ? null : cpf.replaceAll("\\D", "");
        }

        private static String normalizePhone(String telephone) {
                return telephone == null ? null : telephone.replaceAll("\\D", "");
        }
}
