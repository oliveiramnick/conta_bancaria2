package com.senai.conta_bancaria2.aplication.dto;

import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;

@Schema(
        name = "ClienteRegistroDTO",
        description = "Objeto utilizado para registrar ou atualizar informações de um cliente, incluindo seus dados pessoais e conta vinculada."
)
public record ClienteRegistroDTO(


        @Schema(
                description = "Nome completo do cliente.",
                example = "Maria Oliveira"
        )
        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @Schema(
                description = "CPF do cliente (somente números). Deve ser único no sistema.",
                example = "12345678900"
        )
        @NotBlank(message = "CPF é obrigatório.")
        String cpf,

        @Schema(
                description = "E-mail do cliente utilizado para login e contato.",
                example = "maria.oliveira@gmail.com"
        )
        @NotBlank(message = "E-mail é obrigatório.")
        String email,

        @Schema(
                description = "Senha do cliente. Deve conter no mínimo 6 caracteres.",
                example = "senhaSegura123"
        )
        @NotBlank(message = "Senha é obrigatória.")
        String senha,

        @Schema(
                description = "Objeto contendo informações resumidas da conta associada ao cliente.",
                implementation = ContaResumoDTO.class
        )
        @Valid
        ContaResumoDTO contaDTO
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .role(Role.CLIENTE)
                .contas(new ArrayList<>())
                .build();
    }
}