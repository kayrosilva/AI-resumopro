package com.resumopro.ai_resumopro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class ResumoService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String gerarResumo(String texto) {
        try {
            // Montar payload JSON para Chat Completion
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "Você é um assistente que resume textos."));
            messages.add(Map.of("role", "user", "content", "Resuma o seguinte texto:\n\n" + texto));
            requestBody.put("messages", messages);

            String jsonRequest = objectMapper.writeValueAsString(requestBody);

            // Criar requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Resposta OpenAI completa: " + responseBody);

            // Usar JsonNode para melhor navegação e evitar ClassCastException
            JsonNode root = objectMapper.readTree(responseBody);

            // Verificar se retornou erro da API
            if (root.has("error")) {
                String errorMsg = root.get("error").get("message").asText();
                return "Erro da OpenAI: " + errorMsg;
            }

            JsonNode choices = root.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode firstChoice = choices.get(0);
                JsonNode message = firstChoice.get("message");
                if (message != null && message.has("content")) {
                    return message.get("content").asText();
                }
            }

            return "Erro: resposta sem resumo.";

        } catch (Exception e) {
            return "Erro ao conectar com a OpenAI: " + e.getMessage();
        }
    }
}
