package com.senai.conta_bancaria2.domain.exceptions;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException() {
        super("Tipo de conta inválida. Os tipos válidos são: 'CORRENTE' ou 'POUPANÇA'");
    }
}
