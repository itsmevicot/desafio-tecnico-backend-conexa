package com.conexa.backend.scheduling.presentation.api.v1.mappers;

import com.conexa.backend.scheduling.domain.models.Patient;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.PatientRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.PatientResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    /**
     * Maps a Patient entity to a PatientResponseDTO.
     *
     * @param patient the Patient entity
     * @return the corresponding PatientResponseDTO
     */
    public PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getCpf(),
                patient.isActive()
        );
    }

    /**
     * Maps a PatientRequestDTO to a Patient entity.
     *
     * @param request the PatientRequestDTO
     * @return the corresponding Patient entity
     */
    public Patient toEntity(PatientRequestDTO request) {
        return Patient.builder()
                .name(request.name())
                .cpf(request.cpf())
                .isActive(true)
                .build();
    }
}
