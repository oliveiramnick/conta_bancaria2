package com.senai.conta_bancaria2.infrastructure.mqtt;

import com.senai.conta_bancaria2.aplication.service.PagamentoAppService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class MqttListener {

    private final MqttClient client;
    private final PagamentoAppService pagamentoAppService;

    public MqttListener(MqttClient client, PagamentoAppService pagamentoAppService) {
        this.client = client;
        this.pagamentoAppService = pagamentoAppService;
    }

    @PostConstruct
    public void init() throws MqttException {
        if (!client.isConnected()) {
            client.connect();
        }

        // Assina todos os tópicos: banco/validacao/{clienteId}
        client.subscribe("banco/validacao/+", (topic, message) -> {
            try {
                String[] parts = topic.split("/");
                if (parts.length < 3) {
                    return;
                }

                String clienteId = parts[2];
                String payload = new String(message.getPayload());

                if (payload.startsWith("CODE:")) {
                    String codigo = payload.substring(5);
                    // ✅ O método validarCodigo agora existe no serviço
                    pagamentoAppService.validarCodigo(clienteId, codigo);
                    System.out.println("Código IoT validado para cliente " + clienteId);
                }

            } catch (Exception e) {
                // Em produção, use um logger (ex: SLF4J)
                e.printStackTrace();
            }
        });

        System.out.println("MqttListener assinando tópicos: banco/validacao/+");
    }
}
