package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.aplication.dto.ClienteRegistroDTO;
import com.senai.conta_bancaria2.aplication.dto.ClienteResponseDTO;
import com.senai.conta_bancaria2.aplication.service.ClienteService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes bancários: cadastro, consulta, atualização e exclusão.")
@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@Transactional
public class ClienteController {

    private final ClienteService service;


    @Operation(
            summary = "Cadastrar um novo cliente",
            description = "Registra um novo cliente no sistema ou anexa uma conta existente a ele, após validações de CPF e e-mail.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de cadastro de cliente",
                                    value = """
                                            {
                                              "nome": "Ana Souza",
                                              "cpf": "987.654.321-00",
                                              "email": "ana.souza@email.com",
                                              "telefone": "(11) 99999-8888",
                                              "numeroConta": "12345-6"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação dos dados de entrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "CPF inválido", value = "\"CPF informado é inválido.\""),
                                            @ExampleObject(name = "Campo obrigatório", value = "\"O campo 'nome' é obrigatório.\""),
                                            @ExampleObject(name = "Email inválido", value = "\"Formato de e-mail incorreto.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflito de dados",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Já existe um cliente cadastrado com este CPF.\"")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRegistroDTO dto
    ) {
        ClienteResponseDTO novoCliente = service.registarClienteOuAnexarConta(dto);
        return ResponseEntity.created(
                URI.create("/api/cliente/cpf/" + novoCliente.cpf())
        ).body(novoCliente);
    }


    @Operation(
            summary = "Listar todos os clientes ativos",
            description = "Retorna uma lista de todos os clientes ativos cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
            }
    )
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(service.listarClientesAtivos());
    }


    @Operation(
            summary = "Buscar cliente ativo por CPF",
            description = "Retorna os dados de um cliente ativo com base em seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente", example = "987.654.321-00")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado ou inativo",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Cliente com CPF 987.654.321-00 não encontrado.\"")
                            )
                    )
            }
    )
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarClienteAtivoPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarClienteAtivoPorCpf(cpf));
    }


    @Operation(
            summary = "Atualizar dados de um cliente",
            description = "Atualiza informações de um cliente existente (como nome, telefone, e-mail ou conta vinculada).",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser atualizado", example = "987.654.321-00")
            },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ClienteRegistroDTO.class),
                            examples = @ExampleObject(name = "Exemplo de atualização de cliente", value = """
                                    {
                                      "nome": "Ana Lúcia Souza",
                                      "cpf": "987.654.321-00",
                                      "email": "ana.lucia@email.com",
                                      "telefone": "(11) 98888-7777",
                                      "numeroConta": "54321-0"
                                    }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação nos dados atualizados",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "CPF inválido", value = "\"CPF informado é inválido.\""),
                                            @ExampleObject(name = "Email inválido", value = "\"Formato de e-mail incorreto.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Cliente com CPF 987.654.321-00 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable String cpf,
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRegistroDTO dto
    ) {
        return ResponseEntity.ok(service.atualizarCliente(cpf, dto));
    }


    @Operation(
            summary = "Deletar cliente",
            description = "Remove permanentemente um cliente do sistema a partir do seu CPF.",
            parameters = {
                    @Parameter(name = "cpf", description = "CPF do cliente a ser deletado", example = "987.654.321-00")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Cliente com CPF 987.654.321-00 não encontrado.\"")
                            )
                    )
            }
    )
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf) {
        service.deletarCliente(cpf);
        return ResponseEntity.noContent().build();
    }
}
