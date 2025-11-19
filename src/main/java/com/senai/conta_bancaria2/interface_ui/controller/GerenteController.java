package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.aplication.dto.GerenteDTO;
import com.senai.conta_bancaria2.aplication.service.GerenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gerentes", description = "Gerenciamento de gerentes bancários e cadastro de novos responsáveis por contas")
@RestController
@RequestMapping("/gerentes")
@RequiredArgsConstructor
public class GerenteController {
    private final GerenteService service;


    @Operation(
            summary = "Listar todos os gerentes",
            description = "Retorna todos os gerentes cadastrados no sistema, com informações básicas de identificação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de gerentes retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<GerenteDTO>> listarTodosGerentes() {
        return ResponseEntity.ok(service.listarTodosGerentes());
    }


    @Operation(
            summary = "Buscar gerente por ID",
            description = "Retorna os dados completos de um gerente específico a partir do seu ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID do gerente", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Gerente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Gerente com ID 99 não encontrado.\"")
                            ))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<GerenteDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarGerentePorId(id));
    }


    @Operation(
            summary = "Cadastrar um novo gerente",
            description = "Registra um novo gerente bancário.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de cadastro de gerente",
                                    value = """
                                            {
                                              "nome": "João da Silva",
                                              "cpf": "123.456.789-00",
                                              "email": "joao.silva@banco.com",
                                              "senha": "123456"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Gerente cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do gerente"),
                    @ApiResponse(responseCode = "409", description = "Conflito de dados (CPF ou e-mail já existentes)")
            }
    )
    @PostMapping
    public ResponseEntity<GerenteDTO> cadastrarGerente(@Valid @RequestBody GerenteDTO dto) {
        return ResponseEntity.status(201).body(service.cadastrarGerente(dto));
    }


    @Operation(
            summary = "Atualizar informações de um gerente",
            description = "Atualiza os dados de um gerente existente.",
            parameters = {
                    @Parameter(name = "id", description = "ID do gerente a ser atualizado", example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GerenteDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de atualização",
                                    value = """
                                            {
                                              "nome": "João Mendes",
                                              "cpf": "123.456.789-00",
                                              "email": "joao.mendes@banco.com",
                                              "senha": "654321"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Gerente atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Gerente não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<GerenteDTO> atualizarGerente(@PathVariable String id,
                                                       @Valid @RequestBody GerenteDTO dto) {
        return ResponseEntity.ok(service.atualizarGerente(id, dto));
    }


    @Operation(
            summary = "Deletar um gerente",
            description = "Remove um gerente da base de dados a partir do seu ID.",
            parameters = {
                    @Parameter(name = "id", description = "ID do gerente a ser deletado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Gerente removido com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Gerente não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarGerente(@PathVariable String id) {
        service.deletarGerente(id);
        return ResponseEntity.noContent().build();
    }

}
