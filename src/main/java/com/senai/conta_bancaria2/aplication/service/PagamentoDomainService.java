package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.domain.entity.Pagamento;
import com.senai.conta_bancaria2.domain.entity.Taxa;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PagamentoDomainService {
    public BigDecimal calcularValorFinal(Pagamento pagamento) {
        BigDecimal base = pagamento.getValorPago();
        BigDecimal total = base;
        if (pagamento.getTaxa() != null) {
            for (Taxa t : pagamento.getTaxa()) {
                BigDecimal perc = t.getPercentual() == null ? BigDecimal.ZERO : t.getPercentual();
                BigDecimal fixo = t.getValorFixo() == null ? BigDecimal.ZERO : t.getValorFixo();
                BigDecimal valorPerc = base.multiply(perc).divide(BigDecimal.valueOf(100));
                total = total.add(valorPerc).add(fixo);
            }
        }
        return total;
    }
}