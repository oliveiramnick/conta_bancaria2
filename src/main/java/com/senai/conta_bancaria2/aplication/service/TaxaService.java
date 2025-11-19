package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.domain.entity.Taxa;
import com.senai.conta_bancaria2.domain.entity.TaxaDescricao;
import com.senai.conta_bancaria2.domain.repository.TaxaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxaService {
    private final TaxaRepository repo;

    public TaxaService(TaxaRepository repo) {
        this.repo = repo;
    }

    public Taxa criar(TaxaDescricao descricao, BigDecimal percentual, BigDecimal valorFixo) {
        Taxa t = new Taxa();
        t.setDescricao(descricao);
        t.setPercentual(percentual);
        t.setValorFixo(valorFixo);
        return repo.save(t);
    }

    public List<Taxa> listar() {
        return repo.findAll();
    }

    public Taxa buscarPorId(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Taxa não encontrada"));
    }
}
