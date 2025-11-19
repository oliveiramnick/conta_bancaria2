package com.senai.conta_bancaria2.domain.exceptions;

public class DispositivoNaoEncontradoException extends RuntimeException {
    public DispositivoNaoEncontradoException() {
        super("Nenhum dispositivo IoT ativo foi encontrado para o cliente.");
    }
}
