package com.senai.conta_bancaria2.aplication.dto;

import com.senai.conta_bancaria2.domain.entity.Pagamento;
import com.senai.conta_bancaria2.domain.entity.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(
        name = "PagamentoDTO",
        description = "DTO para transportar informações de Pagamento"
)
@Builder
public record PagamentoDTO(

        @Schema(description = "Descrição do pagamento", example = "Pagamento de conta de luz")
        @NotBlank(message = "A descrição do pagamento não pode ser nula ou vazia")
        String boleto,

        @Schema(description = "data do pagamento não pode ser nula", example = "2024-12-31")
        @NotBlank(message = "A data do pagamento não pode ser nula ou vazia")
        String dataPagamento,


        @Schema(description = "Valor do pagamento", example = "150.75")
        @NotNull(message = "O valor do pagamento não pode ser nulo")
        @Digits(integer = 15, fraction = 2, message = "O valor do pagamento deve ter até 15 dígitos inteiros e 2 decimais")
        BigDecimal valorPago,

        @Schema(description = "Status do pagamento (enum)", example = "PENDENTE")
        @NotNull(message = "O status do pagamento não pode ser nulo")
        StatusPagamento status) {
    public static PagamentoDTO fromEntity(Pagamento pagamento) {
        return PagamentoDTO.builder()
                .boleto(pagamento.getBoleto())
                .dataPagamento(String.valueOf(pagamento.getDataPagamento()))
                .valorPago(pagamento.getValorPago())
                .status(pagamento.getStatus())
                .build();
    }

    public static Pagamento toEntity(PagamentoDTO pagamentoDTO) {
        return Pagamento.builder()
                .boleto(pagamentoDTO.boleto)
                .dataPagamento(LocalDateTime.parse(pagamentoDTO.dataPagamento))
                .valorPago(pagamentoDTO.valorPago)
                .status(pagamentoDTO.status)
                .build();

    }
}

