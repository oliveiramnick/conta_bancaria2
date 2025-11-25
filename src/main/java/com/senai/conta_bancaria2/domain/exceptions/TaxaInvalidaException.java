package com.senai.conta_bancaria2.domain.exceptions;

public class TaxaInvalidaException extends RuntimeException {
    public TaxaInvalidaException() {
        super("Taxa inválida");
    }
}