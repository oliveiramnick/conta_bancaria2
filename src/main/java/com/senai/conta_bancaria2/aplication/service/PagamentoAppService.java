package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.domain.entity.*;
import com.senai.conta_bancaria2.domain.exceptions.AutenticacaoIoTExpiradaException;
import com.senai.conta_bancaria2.domain.exceptions.PagamentoInvalidoException;
import com.senai.conta_bancaria2.domain.exceptions.SaldoInsuficienteException;
import com.senai.conta_bancaria2.domain.repository.*;
import com.senai.conta_bancaria2.domain.service.PagamentoDomainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PagamentoAppService {
        private final PagamentoRepository pagamentoRepository;
        private final TaxaRepository taxaRepository;
        private final CodigoAutenticacaoRepository codigoRepo;
        private final DispositivoIoTRepository dispositivoRepo;
        private final PagamentoDomainService domainService;
        private final ContaService contaService;
        private final ClienteService clienteService;

        public PagamentoAppService(PagamentoRepository pagamentoRepository,
                                   TaxaRepository taxaRepository,
                                   CodigoAutenticacaoRepository codigoRepo,
                                   DispositivoIoTRepository dispositivoRepo,
                                   PagamentoDomainService domainService,
                                   ContaService contaService,
                                   ClienteService clienteService) {
            this.pagamentoRepository = pagamentoRepository;
            this.taxaRepository = taxaRepository;
            this.codigoRepo = codigoRepo;
            this.dispositivoRepo = dispositivoRepo;
            this.domainService = domainService;
            this.contaService = contaService;
            this.clienteService = clienteService;
        }

        @Transactional
        public CodigoAutenticacao iniciarAutenticacao(String clienteId) {
            Cliente cliente = clienteService.buscarPorId(clienteId);

            dispositivoRepo.findByClienteAndAtivoTrue(cliente)
                    .orElseThrow(PagamentoInvalidoException::new);

            String codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            CodigoAutenticacao auth = new CodigoAutenticacao();
            auth.setCliente(cliente);
            auth.setCodigo(codigo);
            auth.setValidado(false);
            auth.setExpiraEm(LocalDateTime.now().plusMinutes(2));
            codigoRepo.save(auth);

            String idClienteTopico = String.valueOf(cliente.getId());

            return auth;
        }

        @Transactional
        public void validarCodigo(String clienteId, String codigo) {
            Cliente cliente = clienteService.buscarPorId(clienteId);

            CodigoAutenticacao ultimo = codigoRepo.findTopByClienteOrderByIdDesc(cliente)
                    .orElseThrow(AutenticacaoIoTExpiradaException::new);

            if (LocalDateTime.now().isAfter(ultimo.getExpiraEm())) {
                throw new AutenticacaoIoTExpiradaException();
            }

            if (!ultimo.getCodigo().equals(codigo)) {
                throw new PagamentoInvalidoException();
            }

            ultimo.setValidado(true);
            codigoRepo.save(ultimo);

            String idClienteTopico = String.valueOf(cliente.getId());
        }

        @Transactional
        public Pagamento confirmarPagamento(String contaId,
                                            String clienteId,
                                            String boleto,
                                            LocalDate dataVencimento,
                                            BigDecimal valorPrincipal,
                                            List<String> taxaIds) {

            Cliente cliente = clienteService.buscarPorId(clienteId);
            Conta conta = contaService.buscarPorId(contaId);

            CodigoAutenticacao ultimo = codigoRepo.findTopByClienteOrderByIdDesc(cliente)
                    .orElseThrow(AutenticacaoIoTExpiradaException::new);

            if (!ultimo.isValidado() || LocalDateTime.now().isAfter(ultimo.getExpiraEm())) {
                throw new AutenticacaoIoTExpiradaException();
            }

            if (dataVencimento != null && dataVencimento.isBefore(LocalDate.now())) {
                throw new PagamentoInvalidoException();
            }

            // Busca taxas e transforma em Set
            List<Taxa> taxasList = taxaRepository.findAllById(taxaIds);
            Set<Taxa> taxaSet = new HashSet<>(taxasList);

            // Monta entidade Pagamento usando builder
            Pagamento p = Pagamento.builder()
                    .conta(conta)
                    .boleto(boleto)
                    .valorPago(valorPrincipal != null ? valorPrincipal : null)
                    .dataPagamento(LocalDateTime.now())
                    .taxa(taxaSet.iterator().next()) // Assume que a entidade Pagamento tem um campo Set<Taxas> chamado 'taxa'
                    .build();

            // Calcula valor final (valor principal + soma das taxas)
            BigDecimal valorFinal = valorPrincipal != null ? valorPrincipal : BigDecimal.ZERO;
            for (Taxa t : taxaSet) {
                valorFinal = valorFinal.add(t.getValor()); // Agora, getValor() retorna BigDecimal
            }

            try {
                conta.sacar(valorFinal);
            } catch (SaldoInsuficienteException e) {
                p.setStatus(StatusPagamento.SALDO_INSUFICIENTE);
                pagamentoRepository.save(p);
                throw e;
            }

            p.setStatus(StatusPagamento.SUCESSO);
            return pagamentoRepository.save(p);
        }
    }
