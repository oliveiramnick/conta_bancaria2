package com.senai.conta_bancaria2.domain.repository;
import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, String> {

    List<Conta> findAllByAtivaTrue();  // Está correto
    Optional<Conta> findByIdAndAtivaTrue(String cpf);  // Corrigir 'Ativo' para 'Ativa'
    Optional<Conta> findByNumeroAndAtivaTrue(String numero);  // Está correto
}

