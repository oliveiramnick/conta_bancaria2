package com.senai.conta_bancaria2.domain.service;

import com.senai.conta_bancaria2.domain.entity.Conta;
import com.senai.conta_bancaria2.domain.entity.Pagamento;
import com.senai.conta_bancaria2.domain.entity.StatusPagamento;
import com.senai.conta_bancaria2.domain.entity.Taxa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagamentoDomainService {

    public PagamentoResult processarPagamento(
            Conta conta,
            String codigoBoleto,
            LocalDateTime vencimentoBoleto,
            BigDecimal valorBoleto,
            Taxa taxa
    ) {

        // 1 — Verifica vencimento
        if (vencimentoBoleto.isBefore(LocalDateTime.now())) {
            return PagamentoResult.fail("Boleto vencido.");
        }

        BigDecimal taxaPercentual = BigDecimal.valueOf(valorBoleto.doubleValue())
                .multiply(taxa.getPercentual().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

        BigDecimal taxaFixa = taxa.getValorFixo(); // taxaFixa já é BigDecimal, sem necessidade de conversão.

        BigDecimal totalTaxas = taxaPercentual.add(taxaFixa);
        // 3 — Valor total do pagamento
        BigDecimal valorTotal = valorBoleto.add(totalTaxas);

        // 4 — Valida saldo
        if (conta.getSaldo().compareTo(valorTotal) < 0) {
            return PagamentoResult.fail("Saldo insuficiente.");
        }

        // 5 — Monta o pagamento (domínio não persiste!)
        Pagamento pagamento = Pagamento.builder()
                .boleto(codigoBoleto)
                .dataPagamento(LocalDateTime.now())
                .valorPago(valorTotal)
                .status(StatusPagamento.SUCESSO)
                .conta(conta)
                .taxa(taxa)
                .build();

        return PagamentoResult.success(pagamento);
    }
}