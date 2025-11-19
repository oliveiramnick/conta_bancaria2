package com.senai.conta_bancaria2.domain.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String message) {

        super(message);
    }
}
