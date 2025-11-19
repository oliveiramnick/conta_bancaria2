package com.senai.conta_bancaria2.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(
        name = "ContaAtualizacaoDTO",
        description = "Objeto utilizado para atualizar os dados financeiros de uma conta bancária, como saldo, limite, rendimento e taxa."
)
public record ContaAtualizacaoDTO(

        @Schema(
                description = "Saldo atual da conta após atualização. Não pode ser negativo.",
                example = "1500.75"
        )
        @NotNull(message = "Saldo não pode ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "Saldo não pode ser negativo")
        @Digits(integer = 15, fraction = 2, message = "Saldo deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal saldo,

        @Schema(
                description = "Limite de crédito disponível na conta.",
                example = "500.00"
        )
        @NotNull(message = "Limite não pode ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "Limite não pode ser negativo")
        @Digits(integer = 15, fraction = 2, message = "Limite deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal limite,

        @Schema(
                description = "Valor acumulado de rendimento da conta. Utilizado em contas com rendimento automático.",
                example = "25.45"
        )
        @NotNull(message = "Rendimento não pode ser nulo")
        @DecimalMin(value = "0.00", inclusive = true, message = "Rendimento não pode ser negativo")
        @Digits(integer = 15, fraction = 2, message = "Rendimento deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal rendimento,

        @Schema(
                description = "Taxa de juros ou manutenção aplicada à conta. Representada em porcentagem decimal (ex: 0.015 = 1,5%).",
                example = "0.015"
        )
        @NotNull(message = "Taxa não pode ser nula")
        @DecimalMin(value = "0.00", inclusive = true, message = "Taxa não pode ser negativa")
        @Digits(integer = 5, fraction = 4, message = "Taxa deve ter até 5 dígitos inteiros e 4 decimais")
        BigDecimal taxa
) {}
