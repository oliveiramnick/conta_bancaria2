package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DispositivoIoT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoSerial;
    private String chavePublica;
    private boolean ativo;

    @OneToOne
    private Cliente cliente;
}
