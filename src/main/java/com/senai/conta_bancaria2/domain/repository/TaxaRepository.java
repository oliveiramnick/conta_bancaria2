package com.senai.conta_bancaria2.domain.repository;
import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.entity.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, String> {
    Optional<Taxa> findByDescricao(String descricao);
    Optional<Cliente> findAllById(String cpf);

    Optional<Object> findById(Long id);
}