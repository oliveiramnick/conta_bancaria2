package com.senai.conta_bancaria2.aplication.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Schema(
        name = "ClienteResponseDTO",
        description = "Objeto retornado nas respostas da API contendo informações detalhadas de um cliente e suas contas associadas."
)
public record ClienteResponseDTO(

        @Schema(
                description = "Identificador único do cliente no sistema.",
                example = "c9f02b1e-91df-4f5e-8b4f-82b97fd3b412"
        )
        @NotBlank(message = "ID não pode ser vazio")
        String id,

        @Schema(
                description = "Nome completo do cliente.",
                example = "João da Silva"
        )
        @NotBlank(message = "Nome não pode ser vazio")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @Schema(
                description = "Endereço de e-mail cadastrado do cliente.",
                example = "joao.silva@gmail.com"
        )
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @Schema(
                description = "Senha de acesso cadastrada pelo cliente. (Normalmente não retornada em APIs públicas por segurança.)",
                example = "senhaSegura123"
        )
        @NotBlank(message = "Senha não pode ser vazia")
        String senha,

        @Schema(
                description = "CPF válido do cliente (somente números).",
                example = "12345678900"
        )
        @NotBlank(message = "CPF não pode ser vazio")
        @CPF(message = "O CPF fornecido não é válido")
        String cpf,

        @Schema(
                description = "Lista de contas bancárias associadas ao cliente.",
                implementation = ContaResumoDTO.class
        )
        List<ContaResumoDTO> contas

) {

    /**
     * Converte uma entidade Cliente para um DTO de resposta detalhado.
     *
     * @param cliente Entidade Cliente vinda do banco de dados.
     * @return Objeto ClienteResponseDTO pronto para retorno em endpoints.
     */
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        List<ContaResumoDTO> contas = cliente.getContas().stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getSenha(),
                cliente.getCpf(),
                contas
        );
    }
}