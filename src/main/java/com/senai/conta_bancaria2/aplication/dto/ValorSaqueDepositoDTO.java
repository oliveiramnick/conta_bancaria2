package com.senai.conta_bancaria2.aplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
@Schema(
        name = "ValorSaqueDepositoDTO",
        description = "Objeto utilizado para operações de saque ou depósito em uma conta, contendo apenas o valor da transação."
)
public record ValorSaqueDepositoDTO(@Schema(
        description = "Valor a ser sacado ou depositado na conta. Deve ser maior que zero e com no máximo 15 dígitos inteiros e 2 decimais.",
        example = "500.00"
)
                                    @NotNull(message = "O valor da transação não pode ser nulo")
                                    @DecimalMin(value = "0.01", inclusive = true, message = "O valor da transação deve ser maior que zero")
                                    @Digits(integer = 15, fraction = 2, message = "O valor da transação deve ter até 15 dígitos inteiros e 2 decimais")
                                    BigDecimal valor
) {}
