package com.senai.conta_bancaria2.domain.exceptions;

public class TransferenciaParaMesmaContaException extends RuntimeException {
    public TransferenciaParaMesmaContaException() {
        super("Não é possível transferir para a mesma conta.");
    }
}
