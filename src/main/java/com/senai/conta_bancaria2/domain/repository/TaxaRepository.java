package com.senai.conta_bancaria2.domain.repository;
import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.entity.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxaRepository extends JpaRepository<Taxa, String> {
    Optional<Taxa> findByDescricao(String descricao);
    Optional<Taxa> findById(String id);  // Certifique-se de que o tipo aqui seja String, ou o tipo do ID da sua entidade
}
