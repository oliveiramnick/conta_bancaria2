package com.senai.conta_bancaria2.aplication.dto;

import com.example.conta_bancaria.domain.entity.Cliente;
import com.example.conta_bancaria.domain.entity.Conta;
import com.example.conta_bancaria.domain.entity.ContaCorrente;
import com.example.conta_bancaria.domain.entity.ContaPoupanca;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

import java.math.BigDecimal;

@Schema(
        name = "ContaResumoDTO",
        description = "Representa um resumo dos dados essenciais de uma conta bancária, incluindo número, tipo e saldo."
)
public record ContaResumoDTO(

        @Schema(
                description = "Número único da conta bancária.",
                example = "1234567890"
        )
        @NotBlank(message = "Número da conta não pode ser vazio")
        @Size(min = 6, max = 20, message = "Número da conta deve ter entre 6 e 20 caracteres")
        String numero,

        @Schema(
                description = "Tipo de conta: pode ser 'CORRENTE' ou 'POUPANCA'.",
                example = "CORRENTE"
        )
        @NotBlank(message = "Tipo de conta não pode ser vazio")
        @Pattern(
                regexp = "^(CORRENTE|POUPANCA)$",
                message = "Tipo de conta deve ser 'CORRENTE' ou 'POUPANCA'"
        )
        String tipo,

        @Schema(
                description = "Saldo atual disponível na conta.",
                example = "2500.50"
        )
        @DecimalMin(value = "0.00", inclusive = true, message = "Saldo não pode ser negativo")
        @Digits(integer = 15, fraction = 2, message = "Saldo deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal saldo
) {

    /**
     * Converte o DTO em uma entidade Conta, vinculando-a a um cliente existente.
     * Cria automaticamente instâncias de ContaCorrente ou ContaPoupanca conforme o tipo informado.
     */
    public Conta toEntity(Cliente cliente) {
        if ("CORRENTE".equalsIgnoreCase(tipo)) {
            return ContaCorrente.builder()
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .ativa(true)
                    .cliente(cliente)
                    .limite(new BigDecimal("500.00"))
                    .taxa(new BigDecimal("0.05"))
                    .build();
        } else if ("POUPANCA".equalsIgnoreCase(tipo)) {
            return ContaPoupanca.builder()
                    .numero(this.numero)
                    .saldo(this.saldo)
                    .ativa(true)
                    .rendimento(new BigDecimal("0.01"))
                    .cliente(cliente)
                    .build();
        }
        return null;
    }

    /**
     * Converte uma entidade Conta em um DTO de resumo, contendo apenas informações principais.
     */
    public static ContaResumoDTO fromEntity(Conta conta) {
        return new ContaResumoDTO(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}


