package com.senai.conta_bancaria2.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import org.springframework.data.annotation.Id;

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
