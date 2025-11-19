package com.senai.conta_bancaria2.aplication.service;

import com.example.conta_bancaria.aplication.dto.ContaAtualizacaoDTO;
import com.example.conta_bancaria.aplication.dto.ContaResumoDTO;
import com.example.conta_bancaria.aplication.dto.TransferenciaDTO;
import com.example.conta_bancaria.aplication.dto.ValorSaqueDepositoDTO;
import com.example.conta_bancaria.domain.entity.Conta;
import com.example.conta_bancaria.domain.entity.ContaCorrente;
import com.example.conta_bancaria.domain.entity.ContaPoupanca;
import com.example.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.example.conta_bancaria.domain.exceptions.RendimentoInvalidoException;
import com.example.conta_bancaria.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository repository;

    @Transactional(readOnly = true)
    public List<ContaResumoDTO> listarTodasContas() {
        return repository.findAllByAtivaTrue().stream()
                .map(ContaResumoDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public ContaResumoDTO buscarContaPorNumero(String numero) {
        return ContaResumoDTO.fromEntity(
                repository.findByNumeroAndAtivaTrue(numero)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"))
        );
    }
    public ContaResumoDTO atualizarConta(String numeroDaConta, ContaAtualizacaoDTO dto){
        var conta = buscarContaAtivaPorNumero(numeroDaConta);

        if (conta instanceof ContaPoupanca poupanca){
            poupanca.setRendimento(dto.rendimento());
        } else if (conta instanceof ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }

        conta.setSaldo(dto.saldo());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    public void deletarConta(String numeroDaConta) {
        var conta = buscarContaAtivaPorNumero(numeroDaConta);
        conta.setAtiva(false);
        repository.save(conta);
    }

    private Conta buscarContaAtivaPorNumero(String numeroDaConta) {
        var conta = repository.findByNumeroAndAtivaTrue(numeroDaConta).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Conta")
        );
        return conta;
    }

    public ContaResumoDTO sacar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
        var conta = buscarContaAtivaPorNumero(numeroDaConta);
        conta.sacar(dto.valor());
       return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    public ContaResumoDTO depositar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
        var conta = buscarContaAtivaPorNumero(numeroDaConta);
        conta.depositar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }
    public ContaResumoDTO transferir(String numeroDaConta, TransferenciaDTO dto){
      var contaOrigem = buscarContaAtivaPorNumero(numeroDaConta);
      var contaDestino = buscarContaAtivaPorNumero(dto.contaDestino());

        contaOrigem.transferir(dto.valor(), contaDestino);

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }

    public ContaResumoDTO aplicarRendimento(String numeroDaConta) {
        var conta  = buscarContaAtivaPorNumero(numeroDaConta);
        if(conta instanceof ContaPoupanca poupanca){
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(conta));
        }
        throw new RendimentoInvalidoException();
    }
}
