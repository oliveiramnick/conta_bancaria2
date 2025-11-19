package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@Table(name="pagamento")
@DiscriminatorValue("PAGAMENTO")

public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String boleto;

    @Column(nullable = false, unique = true)
    private LocalDateTime dataPagamento;

    @Column(nullable = false)
    private BigDecimal valorPago;

    @Column(nullable = false)
    private StatusPagamento status;

    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @ManyToMany
    private Set<Taxa> taxa;

}