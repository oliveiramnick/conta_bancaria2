package com.senai.conta_bancaria2.domain.service;

import com.senai.conta_bancaria2.aplication.service.ClienteService;
import com.senai.conta_bancaria2.aplication.service.ContaService;
import com.senai.conta_bancaria2.aplication.service.PagamentoDomainService;
import com.senai.conta_bancaria2.domain.entity.*;
import com.senai.conta_bancaria2.domain.exceptions.AutenticacaoIoTExpiradaException;
import com.senai.conta_bancaria2.domain.exceptions.PagamentoInvalidoException;
import com.senai.conta_bancaria2.domain.exceptions.SaldoInsuficienteException;
import com.senai.conta_bancaria2.domain.repository.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;
    private final TaxaRepository taxaRepository;
    private final CodigoAutenticacaoRepository codigoRepository;
    private final DispositivoIoTRepository dispositivoRepository;
    private final PagamentoDomainService domainService;
    private final ContaService contaService;
    private final ClienteService clienteService;

    public PagamentoAppService(PagamentoRepository pagamentoRepository,
                               TaxaRepository taxaRepository,
                               CodigoAutenticacaoRepository codigoRepository,
                               DispositivoIoTRepository dispositivoRepository,
                               PagamentoDomainService domainService,
                               ContaService contaService,
                               ClienteService clienteService) {
        this.pagamentoRepository = pagamentoRepository;
        this.taxaRepository = taxaRepository;
        this.codigoRepository = codigoRepository;
        this.dispositivoRepository = dispositivoRepository;
        this.domainService = domainService;
        this.contaService = contaService;
        this.clienteService = clienteService;
    }

    /** Dispara solicitação de autenticação via MQTT e registra o código pendente. */
    @Transactional
    public CodigoAutenticacao iniciarAutenticacao(String cpf) {
        Cliente cliente = clienteService.buscarClienteAtivoPorCpf(cpf);

        // Garante que existe dispositivo IoT ativo para esse cliente
        dispositivoRepository.findByClienteAndAtivoTrue(cliente)
                .orElseThrow(PagamentoInvalidoException::new);

        // Gera código simples de 6 caracteres
        String codigo = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        CodigoAutenticacao auth = new CodigoAutenticacao();
        auth.setCliente(cliente);
        auth.setCodigo(codigo);
        auth.setValidado(false);
        auth.setExpiraEm(LocalDateTime.now().plusMinutes(2));
        codigoRepository.save(auth);

        // Envia o código via MQTT
        String idClienteTopico = String.valueOf(cliente.getId());
        mqtt.enviarCodigoAutenticacao(idClienteTopico, codigo);

        return auth;
    }

    /** Validação do código (pelo listener MQTT ou endpoint). */
    @Transactional
    public void validarCodigo(String clienteId, String codigo) {
        Cliente cliente = clienteService.buscarClienteAtivoPorCpf(clienteId);

        CodigoAutenticacao ultimo = codigoRepository.findTopByClienteOrderByIdDesc(cliente)
                .orElseThrow(AutenticacaoIoTExpiradaException::new);

        if (LocalDateTime.now().isAfter(ultimo.getExpiraEm())) {
            throw new AutenticacaoIoTExpiradaException();
        }

        if (!ultimo.getCodigo().equals(codigo)) {
            throw new PagamentoInvalidoException();
        }

        ultimo.setValidado(true);
        codigoRepo.save(ultimo);

        // Opcional: notifica o dispositivo IoT que a validação deu certo
        String idClienteTopico = String.valueOf(cliente.getId());
        mqtt.enviarConfirmacaoValidacao(idClienteTopico, codigo);
    }

    /** Confirma e processa o pagamento após autenticação IoT válida. */
    @Transactional
    public Pagamento confirmarPagamento(String contaId,
                                        String clienteId,
                                        String boleto,
                                        LocalDate dataVencimento,
                                        BigDecimal valorPrincipal,
                                        List<Long> taxaIds) {

        Cliente cliente = clienteService.buscarPorId(clienteId);
        Conta conta = contaService.buscarPorId(contaId);

        // Autenticação IoT deve estar válida e dentro do prazo
        CodigoAutenticacao ultimo = codigoRepo.findTopByClienteOrderByIdDesc(cliente)
                .orElseThrow(AutenticacaoIoTExpiradaException::new);

        if (!ultimo.isValidado() || LocalDateTime.now().isAfter(ultimo.getExpiraEm())) {
            throw new AutenticacaoIoTExpiradaException();
        }

        // Validação de boleto (não permite pagar boleto vencido)
        if (dataVencimento != null && dataVencimento.isBefore(LocalDate.now())) {
            throw new PagamentoInvalidoException("Pagamento Inválido");
        }

        // Monta entidade Pagamento
        Pagamento p = new Pagamento();
        p.setConta(conta);
        p.setBoleto(boleto);
        p.setValorPago(valorPrincipal);
        p.setDataPagamento(LocalDateTime.now());
        p.setTaxa(new HashSet<>(taxaRepository.findAllById(idTaxa)));

        // Calcula valor final (valor + taxas)
        BigDecimal valorFinal = domainService.calcularValorFinal(p);

        // Tenta sacar — se não houver saldo, salva como SALDO_INSUFICIENTE
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
