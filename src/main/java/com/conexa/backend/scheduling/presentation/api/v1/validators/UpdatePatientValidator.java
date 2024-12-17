package com.conexa.backend.scheduling.presentation.api.v1.validators;

import br.com.caelum.stella.validation.CPFValidator;

import com.conexa.backend.scheduling.domain.exceptions.auth.InvalidCPFException;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.PatientRequestDTO;

public class UpdatePatientValidator {

    private static final CPFValidator cpfValidator = new CPFValidator();

    public static void validate(PatientRequestDTO request) {
        if (!isValidCPF(request.cpf())) {
            throw new InvalidCPFException();
        }
    }

    private static boolean isValidCPF(String cpf) {
        try {
            cpfValidator.assertValid(cpf);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
