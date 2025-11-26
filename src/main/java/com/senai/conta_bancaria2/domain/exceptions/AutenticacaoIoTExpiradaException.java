package com.senai.conta_bancaria2.domain.exceptions;

public class AutenticacaoIoTExpiradaException extends RuntimeException {
    public AutenticacaoIoTExpiradaException() {
        super("A autentificação do dispositivo IoT foi expirada.");
    }
}