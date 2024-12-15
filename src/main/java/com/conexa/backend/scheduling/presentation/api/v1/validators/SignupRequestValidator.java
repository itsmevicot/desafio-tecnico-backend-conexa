package com.conexa.backend.scheduling.presentation.api.v1.validators;

import br.com.caelum.stella.validation.CPFValidator;
import com.conexa.backend.scheduling.domain.exceptions.auth.InvalidCPFException;
import com.conexa.backend.scheduling.domain.exceptions.auth.MustBeAtLeast18YearsOldException;
import com.conexa.backend.scheduling.domain.exceptions.auth.PasswordsDoNotMatchException;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.SignupRequestDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignupRequestValidator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final CPFValidator cpfValidator = new CPFValidator();

    public static void validate(SignupRequestDTO request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        if (!isBirthDateValid(request.birthDate())) {
            throw new MustBeAtLeast18YearsOldException();
        }

        if(!isValidCPF(request.cpf())) {
            throw new InvalidCPFException();
        }
    }

    private static boolean isBirthDateValid(String birthDate) {
        try {
            LocalDate birthDateParsed = LocalDate.parse(birthDate, DATE_FORMAT);
            LocalDate today = LocalDate.now();
            return !birthDateParsed.isAfter(today.minusYears(18));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidCPF(String cpf) {
        try {
            cpfValidator.assertValid(cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
