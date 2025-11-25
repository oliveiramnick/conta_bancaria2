package com.senai.conta_bancaria2.domain.exceptions;

public class PagamentoInvalidoException extends RuntimeException {
    public PagamentoInvalidoException()
    {
        super("Pagamento inválido");
    }
}