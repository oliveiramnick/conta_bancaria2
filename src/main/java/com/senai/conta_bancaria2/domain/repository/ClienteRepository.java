package com.senai.conta_bancaria2.domain.repository;

import com.senai.conta_bancaria2.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
     Optional<Cliente> findByCpfAndAtivoTrue(String cpf);
     Optional<Cliente> findByIdAndAtivoTrue(String cpf);

     List<Cliente> findAllByAtivoTrue();
}
