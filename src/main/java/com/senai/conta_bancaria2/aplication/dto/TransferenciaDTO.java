package com.senai.conta_bancaria2.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(
        name = "TransferenciaDTO",
        description = "Objeto utilizado para realizar transferências entre contas, contendo o valor a ser transferido e a conta destino."
)
public record TransferenciaDTO(
        @Schema(
                description = "Valor da transferência. Deve ser maior que zero e com no máximo 15 dígitos inteiros e 2 decimais.",
                example = "250.50"
        )
        @DecimalMin(value = "0.01", inclusive = true, message = "O valor da transferência deve ser maior que zero")
        @Digits(integer = 15, fraction = 2, message = "O valor da transferência deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal valor,

        @Schema(
                description = "Número da conta destino da transferência. Deve conter apenas números e ter entre 6 e 20 caracteres.",
                example = "1234567890"
        )
        @NotBlank(message = "A conta destino não pode ser vazia")
        @Size(min = 6, max = 20, message = "O número da conta destino deve ter entre 6 e 20 caracteres")
        @Pattern(regexp = "^[0-9]+$", message = "O número da conta destino deve conter apenas números")
        String contaDestino
) {}
