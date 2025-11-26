package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CodigoAutenticacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private LocalDateTime expiraEm;
    private boolean validado;

    @ManyToOne
    private Cliente cliente;
}
