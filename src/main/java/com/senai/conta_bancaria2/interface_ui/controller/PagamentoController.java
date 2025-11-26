package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.aplication.service.PagamentoAppService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class PagamentoController {

    private final PagamentoAppService pagamentos;

    public PagamentoController(PagamentoAppService pagamentos) {
        this.pagamentos = pagamentos;
    }


    @PostMapping("/autenticacao")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> iniciarAutenticacao(@RequestBody IniciarAutenticacaoRequest req) {
        var auth = pagamentos.iniciarAutenticacao(req.clienteId);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/confirmar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Object> confirmar(@RequestBody ConfirmarPagamentoRequest req) {
        return ResponseEntity.ok(
                pagamentos.confirmarPagamento(
                        req.contaId,
                        req.clienteId,
                        req.boleto,
                        (req.dataVencimento == null || req.dataVencimento.isBlank())
                                ? null
                                : LocalDate.parse(req.dataVencimento),
                        (req.valorPrincipal != null ? req.valorPrincipal : BigDecimal.ZERO),
                        req.taxaIds
                )
        );
    }

    public static class IniciarAutenticacaoRequest {
        public String clienteId;
    }

    public static class ConfirmarPagamentoRequest {
        public String contaId;
        public String clienteId;
        public String boleto;
        public String dataVencimento;   // formato: yyyy-MM-dd
        public BigDecimal valorPrincipal;
        public List<String> taxaIds;
    }

}