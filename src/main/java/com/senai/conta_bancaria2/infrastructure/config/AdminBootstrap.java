package com.senai.conta_bancaria2.infrastructure.config;

import com.senai.conta_bancaria2.domain.entity.Cliente;
import com.senai.conta_bancaria2.domain.repository.ClienteRepository;
import com.senai.conta_bancaria2.domain.entity.Gerente;
import com.senai.conta_bancaria2.domain.enums.Role;
import com.senai.conta_bancaria2.domain.repository.GerenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final GerenteRepository gerenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        gerenteRepository.findByEmail(adminEmail).ifPresentOrElse(
                gerente -> {
                    if (!gerente.isAtivo()) {
                        gerente.setAtivo(true);
                        gerenteRepository.save(gerente);
                    }
                },
                () -> {
                    Gerente admin = Gerente.builder()
                            .nome("Administrador Provisório")
                            .email(adminEmail)
                            .cpf("000.000.000-00")
                            .senha(passwordEncoder.encode(adminSenha))
                            .role(Role.ADMIN)
                            .build();
                    gerenteRepository.save(admin);
                    System.out.println("⚡ Usuário admin provisório criado: " + adminEmail);
                }
        );
    }
}
