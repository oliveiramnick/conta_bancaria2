package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.aplication.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria2.aplication.dto.ClienteResponseDTO;
import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.exceptions.ContaMesmoTipoException;
import com.senai.conta_bancaria2.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria2.domain.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    private final ClienteRepository repository;

    private final PasswordEncoder passwordEncoder;


    public ClienteResponseDTO registarClienteOuAnexarConta(ClienteRegistroDTO dto) {
        var cliente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );

        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream()
                .anyMatch(c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva());

        if(jaTemTipo)
            throw new ContaMesmoTipoException();

        cliente.getContas().add(novaConta);

        cliente.setSenha(passwordEncoder.encode(dto.senha()));

        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    public ClienteResponseDTO buscarClienteAtivoPorCpf(String cpf) {
        var cliente = buscarPorCpfClienteAtivo(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    public ClienteResponseDTO atualizarCliente(String cpf, ClienteRegistroDTO dto) {
        var cliente = buscarPorCpfClienteAtivo(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    public void deletarCliente(String cpf) {
        var cliente = buscarPorCpfClienteAtivo(cpf);

        cliente.setAtivo(false);
        cliente.getContas().forEach(
                conta -> conta.setAtiva(false)
        );
        repository.save(cliente);
    }

    private Cliente buscarPorCpfClienteAtivo(String cpf) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Cliente")
        );
        return cliente;
    }

    public ClienteResponseDTO buscarClientePorCpf(@Valid String cpf) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente com CPF " + cpf));
        return ClienteResponseDTO.fromEntity(cliente);
    }

}