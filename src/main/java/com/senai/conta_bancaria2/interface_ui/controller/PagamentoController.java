package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.domain.service.PagamentoAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PagamentoController {
    private final PagamentoAppService pagamentos;

    public PagamentoController(PagamentoAppService pagamentos) {
        this.pagamentos = pagamentos;
    }

    /**
     * Inicia a autenticação via dispositivo IoT para o cliente.
     * Gera um código, salva no banco e envia via MQTT (banco/autenticacao/{clienteId}).
     */
    @PostMapping("/autenticacao")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> iniciarAutenticacao(@RequestBody IniciarAutenticacaoRequest req) {
        var auth = pagamentos.iniciarAutenticacao(req.clienteId);
        return ResponseEntity.ok(auth);
    }

    /**
     * Confirma o pagamento após a autenticação IoT ter sido validada.
     */
    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> confirmar(@RequestBody ConfirmarPagamentoRequest req) {
        return ResponseEntity.ok(
                pagamentos.confirmarPagamento(
                        req.contaId,
                        req.clienteId,
                        req.boleto,
                        (req.dataVencimento == null || req.dataVencimento.isBlank())
                                ? null
                                : LocalDate.parse(req.dataVencimento),
                        (req.valorPrincipal != null ? req.valorPrincipal : BigDecimal.ZERO),
                        req.idTaxa
                )
        );
    }

    // ===================== DTOs de request (simples) =====================

    public static class IniciarAutenticacaoRequest {
        public String clienteId;
    }

    public static class ConfirmarPagamentoRequest {
        public String contaId;
        public String clienteId;
        public String boleto;
        public String dataVencimento;   // formato: yyyy-MM-dd
        public BigDecimal valorPrincipal;
        public List<String> idTaxa;
    }
}