package com.conexa.backend.scheduling.presentation.api.v1.validators;

import br.com.caelum.stella.validation.CPFValidator;
import com.conexa.backend.scheduling.domain.exceptions.schedule.InvalidScheduleDateException;
import com.conexa.backend.scheduling.domain.exceptions.auth.InvalidCPFException;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.CreateScheduleRequestDTO;

import java.time.LocalDateTime;

public class CreateScheduleRequestValidator {

    private static final CPFValidator cpfValidator = new CPFValidator();

    public static void validate(CreateScheduleRequestDTO request) {
        validateDateTime(request.dateTime());
        validatePatientCPF(request.patient().cpf());
    }

    private static void validateDateTime(LocalDateTime dateTime) {
        if (dateTime == null || dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidScheduleDateException();
        }
    }

    private static void validatePatientCPF(String cpf) {
        try {
            cpfValidator.assertValid(cpf);
        } catch (Exception e) {
            throw new InvalidCPFException();
        }
    }
}
