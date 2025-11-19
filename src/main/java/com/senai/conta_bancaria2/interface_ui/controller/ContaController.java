package com.senai.conta_bancaria2.interface_ui.controller;


import com.senai.conta_bancaria2.aplication.dto.ContaAtualizacaoDTO;
import com.senai.conta_bancaria2.aplication.dto.ContaResumoDTO;
import com.senai.conta_bancaria2.aplication.dto.TransferenciaDTO;
import com.senai.conta_bancaria2.aplication.dto.ValorSaqueDepositoDTO;
import com.senai.conta_bancaria2.aplication.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Java
@Tag(name = "Contas Bancárias", description = "Gerenciamento completo de contas bancárias e operações financeiras")
@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;


    @Operation(
            summary = "Listar todas as contas",
            description = "Retorna todas as contas cadastradas no sistema com seus dados resumidos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContaResumoDTO>> listarTodasContas() {
        return ResponseEntity.ok(service.listarTodasContas());
    }

    @Operation(
            summary = "Buscar conta por número",
            description = "Retorna os dados de uma conta específica a partir do seu número",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta bancária", example = "12345-6")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @GetMapping("/{numeroDaConta}")
    public ResponseEntity<ContaResumoDTO> buscarContaPorNumero(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.buscarContaPorNumero(numeroDaConta));
    }


    @Operation(
            summary = "Atualizar conta bancária",
            description = "Atualiza os dados de uma conta existente (ex: nome do titular, tipo de conta)",
            parameters = {
                    @Parameter(name = "numeroConta", description = "Número da conta a ser atualizada", example = "12345-6")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaAtualizacaoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                      "nomeTitular": "Maria Oliveira",
                                      "tipoConta": "POUPANCA"
                                    }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @PutMapping("/{numeroConta}")
    public ResponseEntity<ContaResumoDTO> atualizarConta(@PathVariable String numeroConta,
                                                         @Valid @org.springframework.web.bind.annotation.RequestBody ContaAtualizacaoDTO dto) {
        return ResponseEntity.ok(service.atualizarConta(numeroConta, dto));
    }


    @Operation(
            summary = "Deletar conta bancária",
            description = "Remove uma conta bancária existente do sistema pelo número da conta",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta bancária", example = "12345-6")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Conta removida com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/{numeroDaConta}")
    public ResponseEntity<Void> deletarConta(@PathVariable String numeroDaConta) {
        service.deletarConta(numeroDaConta);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Realizar saque",
            description = "Efetua o saque de um valor específico de uma conta bancária, validando saldo disponível",
            parameters = {
                    @Parameter(name = "numeroConta", description = "Número da conta", example = "12345-6")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de saque", value = """
                                    {
                                      "valor": 200.00
                                    }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Saque realizado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Saldo insuficiente ou valor inválido",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Saldo insuficiente", value = "\"Saldo insuficiente para saque.\""),
                                            @ExampleObject(name = "Valor inválido", value = "\"Valor de saque deve ser maior que zero.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<ContaResumoDTO> sacar(@PathVariable String numeroConta,
                                                @Valid @org.springframework.web.bind.annotation.RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.sacar(numeroConta, dto));
    }


    @Operation(
            summary = "Realizar depósito",
            description = "Adiciona um valor à conta bancária especificada",
            parameters = {
                    @Parameter(name = "numeroConta", description = "Número da conta", example = "12345-6")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ValorSaqueDepositoDTO.class),
                            examples = @ExampleObject(name = "Exemplo de depósito", value = """
                                    {
                                      "valor": 500.00
                                    }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Depósito realizado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Valor inválido",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Valor de depósito deve ser maior que zero.\"")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<ContaResumoDTO> depositar(@PathVariable String numeroConta,
                                                    @Valid @org.springframework.web.bind.annotation.RequestBody ValorSaqueDepositoDTO dto) {
        return ResponseEntity.ok(service.depositar(numeroConta, dto));
    }


    @Operation(
            summary = "Realizar transferência",
            description = "Transfere um valor de uma conta de origem para uma conta de destino",
            parameters = {
                    @Parameter(name = "numeroConta", description = "Número da conta de origem", example = "12345-6")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TransferenciaDTO.class),
                            examples = @ExampleObject(name = "Exemplo de transferência", value = """
                                    {
                                      "numeroContaDestino": "98765-4",
                                      "valor": 300.00
                                    }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Saldo insuficiente ou valor inválido",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Saldo insuficiente", value = "\"Saldo insuficiente para transferência.\""),
                                            @ExampleObject(name = "Valor inválido", value = "\"Valor da transferência deve ser maior que zero.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta de origem ou destino não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta destino 98765-4 não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numeroConta}/transferir")
    public ResponseEntity<ContaResumoDTO> transferir(@PathVariable String numeroConta,
                                                     @Valid @org.springframework.web.bind.annotation.RequestBody TransferenciaDTO dto) {
        return ResponseEntity.ok(service.transferir(numeroConta, dto));
    }


    @Operation(
            summary = "Aplicar rendimento",
            description = "Aplica rendimento mensal (juros) sobre o saldo da conta poupança",
            parameters = {
                    @Parameter(name = "numeroDaConta", description = "Número da conta", example = "12345-6")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rendimento aplicado com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Conta não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Conta com número 99999-9 não encontrada.\"")
                            )
                    )
            }
    )
    @PostMapping("/{numeroDaConta}/rendimento")
    public ResponseEntity<ContaResumoDTO> aplicarRendimento(@PathVariable String numeroDaConta) {
        return ResponseEntity.ok(service.aplicarRendimento(numeroDaConta));
    }
}

