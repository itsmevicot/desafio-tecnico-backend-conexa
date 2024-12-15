package com.conexa.backend.scheduling.domain.models;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "doctors")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Specialty is mandatory")
    private String specialty;

    @Column(nullable = false, unique = true)
    @CPF(message = "Invalid CPF format")
    private String cpf;

    @Column(name = "birth_date", nullable = false)
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Column(nullable = false)
    @NotBlank(message = "Phone number is mandatory")
    private String phone;

    @Column(nullable = false)
    private boolean isActive = true;
}
