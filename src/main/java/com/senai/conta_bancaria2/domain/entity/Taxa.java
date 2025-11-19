package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@Table(name = "taxa")

public class Taxa {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String idTaxa;

    @Column (nullable = false, unique = true)
    @Enumerated (EnumType.STRING)
    private TaxaDescricao descricao;

    @Column(nullable = false)
    private BigDecimal percentual;

    @Column(nullable = false)
    private BigDecimal valorFixo;


}