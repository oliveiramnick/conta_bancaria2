package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data

public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private TaxaDescricao descricao;

    @Column(nullable = false)
    private BigDecimal percentual;

    @Column(nullable = false)
    private BigDecimal valorFixo;

    // Metodo para obter o valor da taxa
    public BigDecimal getValor() {
        return valorFixo;
    }
}