package com.senai.conta_bancaria2.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Banco IoT", version = "2.0",
        description = "Pagamentos com Taxas e Autenticação IoT (MQTT)"))
public class OpenAPIConfig {
}
