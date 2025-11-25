package com.senai.conta_bancaria2.aplication.service;

import com.senai.conta_bancaria2.aplication.dto.AuthDTO;
import com.senai.conta_bancaria2.domain.entity.Usuario;
import com.senai.conta_bancaria2.domain.exceptions.UsuarioNaoEncontradoException;
import com.senai.conta_bancaria2.domain.repository.UsuarioRepository;
import com.senai.conta_bancaria2.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private UsuarioRepository usuarios;
    private PasswordEncoder encoder;
    private JwtService jwt;


    public String login(AuthDTO.LoginRequest req) {
        // Busca o usuário pelo e-mail no repositório
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() -> new UsuarioNaoEncontradoException());

        // Valida a senha usando o PasswordEncoder
        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        // Gera e retorna o token JWT contendo e-mail e role do usuário
        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}
