package com.senai.conta_bancaria2.interface_ui.controller;

import com.senai.conta_bancaria2.aplication.dto.AuthDTO;
import com.senai.conta_bancaria2.aplication.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Autenticação",
        description = "Endpoints para login e geração de token JWT."
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário com e-mail e senha válidos e retorna um token JWT para acesso às rotas protegidas.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Credenciais de login do usuário",
                    content = @Content(
                            schema = @Schema(implementation = AuthDTO.LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Login",
                                    value = """
                                            {
                                              "email": "admin@banco.com",
                                              "senha": "admin123"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login realizado com sucesso — token JWT retornado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Resposta com Token",
                                            value = """
                                                    {
                                                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida (dados ausentes ou mal formatados)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Campo ausente", value = "\"O campo 'email' é obrigatório.\""),
                                            @ExampleObject(name = "Formato incorreto", value = "\"Formato de e-mail inválido.\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciais incorretas",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(name = "Senha incorreta", value = "\"Senha incorreta.\""),
                                            @ExampleObject(name = "Usuário não encontrado", value = "\"Usuário não encontrado.\"")
                                    }
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.TokenResponse> login(
            @org.springframework.web.bind.annotation.RequestBody AuthDTO.LoginRequest req
    ) {
        String token = auth.login(req);
        return ResponseEntity.ok(new AuthDTO.TokenResponse(token));
    }
}
