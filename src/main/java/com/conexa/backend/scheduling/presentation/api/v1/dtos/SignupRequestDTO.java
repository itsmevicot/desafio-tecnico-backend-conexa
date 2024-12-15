package com.conexa.backend.scheduling.presentation.api.v1.dtos;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @AssertTrue(message = "Passwords do not match")
    public boolean passwordsMatch() {
        return password.equals(confirmPassword);
    }

    @AssertTrue(message = "You must be at least 18 years old")
    public boolean isBirthDateValid() {
        try {
            LocalDate birthDateParsed = LocalDate.parse(birthDate, DATE_FORMAT);
            LocalDate today = LocalDate.now();
            return !birthDateParsed.isAfter(today.minusYears(18));
        } catch (Exception e) {
            return false;
        }
    }
}
