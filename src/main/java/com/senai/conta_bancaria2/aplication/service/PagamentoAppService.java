package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.domain.entity.*;
import com.senai.conta_bancaria2.domain.repository.*;
import com.senai.conta_bancaria2.domain.service.PagamentoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagamentoAppService {

    private final ContaRepository contaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final TaxaRepository taxasRepository;
    private final PagamentoDomainService pagamentoDomainService;

    // --- NOVO MÉTODO REQUERIDO PELO MqttListener ---
    /**
     * Valida o código recebido do dispositivo IoT para autenticação.
     */
    public void validarCodigo(String clienteId, String codigo) {
        // 💡 LÓGICA NECESSÁRIA:
        // 1. Você deve implementar aqui a lógica para verificar se o 'codigo'
        // é válido para o 'clienteId'.
        // 2. Após a validação, você geralmente realiza a transação que estava
        // pendente ou libera o próximo passo no fluxo de autenticação/pagamento.

        System.out.println("Ação: Validando código '" + codigo + "' para cliente ID: " + clienteId);
        // Exemplo: if (autenticacaoService.isCodigoValido(clienteId, codigo)) { ... }
    }

    // --- Seu método de Pagamento Original ---
    public Pagamento realizarPagamento(
            String contaId,
            String codigoBoleto,
            LocalDateTime vencimento,
            Double valor,
            String taxaId
    ) {

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        Taxa taxa = taxasRepository.findById(Long.valueOf(taxaId))
                .orElseThrow(() -> new RuntimeException("Taxa não encontrada"));

        // Chama regras de negócio
        PagamentoResult resultado = pagamentoDomainService.processarPagamento(
                conta,
                codigoBoleto,
                vencimento,
                valor,
                taxa
        );

        if (!resultado.isSucesso()) {
            throw new RuntimeException("Falha no pagamento: " + resultado.getMensagem());
        }

        Pagamento pagamento = resultado.getPagamento();

        // 💡 CORREÇÃO DE TIPAGEM: Converte o valor para BigDecimal, que é o tipo que conta.sacar() espera.
        // O método getValorPago().doubleValue() foi substituído pela criação segura de BigDecimal.
        // O valor de pagamento.getValorPago() é assumido ser um Double ou Number.
        BigDecimal valorDebito = BigDecimal.valueOf(pagamento.getValorPago().doubleValue());

        // Debita o saldo
        conta.sacar(valorDebito);

        contaRepository.save(conta);
        pagamentoRepository.save(pagamento);

        return pagamento;
    }
}