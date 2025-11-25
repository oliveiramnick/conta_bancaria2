package com.senai.conta_bancaria2.domain.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("Não foi possivel encontrar esse usuario");
    }
}