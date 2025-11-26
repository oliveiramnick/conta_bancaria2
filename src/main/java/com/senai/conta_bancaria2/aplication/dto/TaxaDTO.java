package com.senai.conta_bancaria2.aplication.dto;

import com.senai.conta_bancaria2.domain.entity.Taxa;
import com.senai.conta_bancaria2.domain.entity.TaxaDescricao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Schema(
        name = "TaxaDTO",
        description = "DTO para transportar informações de Taxas"
)
@Builder
public record TaxaDTO(
        @Schema(description = "ID da taxa (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        String id,

        @Schema(description = "Descrição da taxa (enum)", example = "TAXA_MANUTENCAO_CONTA")
        @NotNull(message = "A descrição da taxa não pode ser nula")
        TaxaDescricao descricao,

        @Schema(description = "Percentual aplicado à taxa", example = "2.50")
        @NotNull(message = "O percentual não pode ser nulo")
        @Digits(integer = 3, fraction = 2, message = "O percentual deve ter até 3 dígitos inteiros e 2 decimais")
        BigDecimal percentual,

        @Schema(description = "Valor fixo da taxa", example = "20.00")
        @NotNull(message = "O valor fixo não pode ser nulo")
        @Digits(integer = 10, fraction = 2, message = "O valor fixo deve ter até 10 dígitos inteiros e 2 decimais")
        Double valorFixo
) {
    public static TaxaDTO fromEntity(Taxa taxas) {
        return TaxaDTO.builder()
                .id(taxas.getId())
                .descricao(taxas.getDescricao())
                .percentual(taxas.getPercentual())
                .valorFixo(taxas.getValorFixo().doubleValue())
                .build();

    }
    public static TaxaDTO toEntity(Taxa taxas) {
        return TaxaDTO.builder()
                .id(taxas.getId())
                .descricao(taxas.getDescricao())
                .percentual(taxas.getPercentual())
                .valorFixo(taxas.getValorFixo().doubleValue())
                .build();
    }
}


