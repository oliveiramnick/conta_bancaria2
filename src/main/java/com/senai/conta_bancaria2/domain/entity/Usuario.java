package com.senai.conta_bancaria2.domain.entity;

import com.senai.conta_bancaria2.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @NotBlank
    @Column(nullable = false)
    protected String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    protected String cpf; // formato "000.000.000-00" (validação pode ser ampliada)

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected boolean ativo = true;

    @NotBlank
    @Column(nullable = false)
    protected String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
}
