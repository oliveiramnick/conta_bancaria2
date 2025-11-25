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

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Column(nullable = false)
    private Double valorPago;

    @Column(nullable = false)
    private StatusPagamento status;

    // 💡 CAMPO ATIVO ADICIONADO PARA CORRIGIR O ERRO DE INICIALIZAÇÃO DO JPA
    @Column(nullable = false)
    private boolean ativo = true;

    // Associação com a Conta
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    // Associação ManyToOne com Taxa
    @ManyToOne
    @JoinColumn(name = "taxa_id", nullable = true)
    private Taxa taxa;

    // Métodos de lógica de pagamento podem ser adicionados aqui
}