package com.senai.conta_bancaria2.domain.service;

import com.senai.conta_bancaria2.domain.entity.Conta;
import com.senai.conta_bancaria2.domain.entity.Pagamento;
import com.senai.conta_bancaria2.domain.entity.StatusPagamento;
import com.senai.conta_bancaria2.domain.entity.Taxa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagamentoDomainService {

    public PagamentoResult processarPagamento(
            Conta conta,
            String codigoBoleto,
            LocalDateTime vencimentoBoleto,
            Double valorBoleto,
            Taxa taxa
    ) {

        // 1 — Verifica vencimento
        if (vencimentoBoleto.isBefore(LocalDateTime.now())) {
            return PagamentoResult.fail("Boleto vencido.");
        }

        // 2 — Calcula o total da taxa (fixo + percentual)
        BigDecimal taxaPercentual = BigDecimal.valueOf(valorBoleto)
                .multiply(BigDecimal.valueOf(taxa.getPercentual() / 100));

        BigDecimal taxaFixa = BigDecimal.valueOf(taxa.getValorFixo());

        BigDecimal totalTaxas = taxaPercentual.add(taxaFixa);

        // 3 — Valor total do pagamento
        BigDecimal valorTotal = BigDecimal.valueOf(valorBoleto).add(totalTaxas);

        // 4 — Valida saldo
        if (conta.getSaldo().compareTo(valorTotal) < 0) {
            return PagamentoResult.fail("Saldo insuficiente.");
        }

        // 5 — Monta o pagamento (domínio não persiste!)
        Pagamento pagamento = Pagamento.builder()
                .boleto(codigoBoleto)
                .dataPagamento(LocalDateTime.now())
                .valorPago(valorTotal.doubleValue())
                .status(StatusPagamento.SUCESSO)
                .conta(conta)
                .taxa(taxa)
                .build();

        return PagamentoResult.success(pagamento);
    }
}