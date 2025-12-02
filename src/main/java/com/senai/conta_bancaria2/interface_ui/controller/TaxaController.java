package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.domain.entity.Taxa;
import com.senai.conta_bancaria2.domain.repository.TaxaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/taxas")
@SecurityRequirement(name = "bearerAuth")
public class TaxaController {

    private final TaxaRepository repo;

    public TaxaController(TaxaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public List<Taxa> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<Taxa> buscar(@PathVariable String id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<Taxa> criar(@RequestBody Taxa dto) {
        Taxa salvo = repo.save(dto);
        return ResponseEntity.created(URI.create("/taxas/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<Taxa> atualizar(@PathVariable String id, @RequestBody Taxa dto) {
        return repo.findById(id)
                .map(existente -> {
                    existente.setDescricao(dto.getDescricao());
                    existente.setPercentual(dto.getPercentual());
                    existente.setValorFixo(dto.getValorFixo());
                    return ResponseEntity.ok(repo.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<Void> remover(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
