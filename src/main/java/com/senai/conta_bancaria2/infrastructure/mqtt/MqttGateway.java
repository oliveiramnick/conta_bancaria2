package com.senai.conta_bancaria2.infrastructure.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
public class MqttGateway {

    private final MqttClient client;

    public MqttGateway(MqttClient client) {
        this.client = client;
    }

    /**
     * Método genérico para publicar em qualquer tópico.
     */
    public void publicar(String topic, String payload) {
        try {
            if (!client.isConnected()) {
                client.connect();
            }
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            client.publish(topic, message);
            System.out.println("📤 MQTT enviado para [" + topic + "]: " + payload);
        } catch (MqttException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao publicar no MQTT", e);
        }
    }

    /**
     * Envia o código de autenticação para o dispositivo IoT.
     * Tópico: banco/autenticacao/{clienteId}
     * Payload: CODE:XXXXXX
     */
    public void enviarCodigoAutenticacao(String clienteId, String codigo) {
        String topic = "banco/autenticacao/" + clienteId;
        String payload = "CODE:" + codigo;
        publicar(topic, payload);
    }

    /**
     * Envia confirmação de validação.
     * Tópico: banco/validacao/{clienteId}
     * Payload: VALIDADO:XXXXXX
     */
    public void enviarConfirmacaoValidacao(String clienteId, String codigo) {
        String topic = "banco/validacao/" + clienteId;
        String payload = "VALIDADO:" + codigo;
        publicar(topic, payload);
    }
}