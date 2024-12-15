package com.conexa.backend.scheduling.domain.models;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.*;
import br.com.caelum.stella.bean.validation.CPF;


@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(nullable = false, unique = true)
    @CPF(message = "Invalid CPF format")
    private String cpf;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;
}

