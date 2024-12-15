package com.conexa.backend.scheduling.presentation.api.v1.mappers;

import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.SignupRequestDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Doctor toDoctor(SignupRequestDTO signupRequest) {
        return Doctor.builder()
                .email(signupRequest.email())
                .password(signupRequest.password())
                .specialty(signupRequest.specialty())
                .cpf(signupRequest.cpf())
                .birthDate(LocalDate.parse(signupRequest.birthDate(), DATE_FORMAT))
                .telephone(signupRequest.telephone())
                .build();
    }
}
