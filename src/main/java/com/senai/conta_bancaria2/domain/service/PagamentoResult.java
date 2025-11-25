package com.senai.conta_bancaria2.domain.service;

import com.senai.conta_bancaria2.domain.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagamentoResult {

    private boolean sucesso;
    private String mensagem;
    private Pagamento pagamento;

    public static PagamentoResult success(Pagamento pagamento) {
        return new PagamentoResult(true, null, pagamento);
    }

    public static PagamentoResult fail(String mensagem) {
        return new PagamentoResult(false, mensagem, null);
    }
}