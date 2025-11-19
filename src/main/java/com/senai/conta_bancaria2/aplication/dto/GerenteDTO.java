package com.senai.conta_bancaria2.aplication.dto;

import com.example.conta_bancaria.domain.entity.Gerente;
import com.example.conta_bancaria.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
@Schema(
        name = "GerenteDTO",
        description = "Representa um gerente do sistema bancário, contendo informações de identificação, autenticação e status de atividade."
)
@Builder
public record GerenteDTO(
        @Schema(
                description = "Identificador único do gerente (gerado automaticamente pelo sistema).",
                example = "e1b4c1d0-92a1-4f1c-8a21-9b8a3f4d91ef"
        )
        String id,

        @Schema(
                description = "Nome completo do gerente responsável pela administração de contas e clientes.",
                example = "Mariana Costa"
        )
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String nome,

        @Schema(
                description = "CPF do gerente, utilizado para identificação no sistema.",
                example = "123.456.789-00"
        )
        @NotBlank(message = "O CPF é obrigatório.")
        String cpf,

        @Schema(
                description = "E-mail corporativo do gerente utilizado para login e comunicação interna.",
                example = "mariana.costa@banco.com"
        )
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado não é válido.")
        String email,

        @Schema(
                description = "Senha de acesso do gerente (armazenada de forma segura no sistema).",
                example = "senhaSegura123"
        )
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
        String senha,

        @Schema(
                description = "Indica se o gerente está ativo no sistema.",
                example = "true"
        )
        Boolean ativo,

        @Schema(
                description = "Papel (role) do gerente no sistema — geralmente 'GERENTE'.",
                example = "GERENTE"
        )
        Role role
) {

    /**
     * Converte uma entidade {@link Gerente} em um DTO {@link GerenteDTO}.
     * Útil para retornar informações seguras na API sem expor dados sensíveis.
     */
    public static GerenteDTO fromEntity(Gerente gerente) {
        return GerenteDTO.builder()
                .id(gerente.getId())
                .nome(gerente.getNome())
                .cpf(gerente.getCpf())
                .email(gerente.getEmail())
                .ativo(gerente.isAtivo())
                .role(gerente.getRole())
                .build();
    }

    /**
     * Converte o DTO {@link GerenteDTO} em uma entidade {@link Gerente},
     * aplicando valores padrão para os campos não informados.
     */
    public Gerente toEntity() {
        return Gerente.builder()
                .id(this.id)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .ativo(this.ativo != null ? this.ativo : true)
                .role(this.role != null ? this.role : Role.CLIENTE)
                .build();
    }
}
