package com.conexa.backend.scheduling.presentation.api.v1.mappers;

import com.conexa.backend.scheduling.domain.models.Doctor;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.requests.UpdateDoctorRequestDTO;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.responses.DoctorResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public DoctorResponseDTO toResponseDTO(Doctor doctor) {
        return new DoctorResponseDTO(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getSpecialty(),
                doctor.getCpf(),
                doctor.getBirthDate(),
                doctor.getTelephone()
        );
    }

    public void updateDoctorFromDTO(UpdateDoctorRequestDTO request, Doctor doctor) {
        doctor.setSpecialty(request.specialty());
        doctor.setBirthDate(request.birthDate());
        doctor.setTelephone(request.telephone());
    }
}
