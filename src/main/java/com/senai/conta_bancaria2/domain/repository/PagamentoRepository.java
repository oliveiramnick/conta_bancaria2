package com.senai.conta_bancaria2.domain.repository;
import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
}
