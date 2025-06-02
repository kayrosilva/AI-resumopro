package com.resumopro.ai_resumopro.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HuggingFaceClient {

    @Value("${huggingface.api.token}")
    private String huggingfaceToken;

    private final RestTemplate restTemplate = new RestTemplate();

    // Método para resumir texto com modelo dinâmico
    public String resumirTexto(String textoOriginal, String modelo, String linguagem) {
        String url = "https://api-inference.huggingface.co/models/" + modelo;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingfaceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // Corrigido para aceitar json

        // Montar JSON com inputs e parâmetros extras (exemplo: linguagem, se precisar)
        String requestBody = "{\"inputs\": \"" + textoOriginal.replace("\"", "\\\"") + "\"";

        // Exemplo de adicionar linguagem (depende do modelo e API se aceita)
        if (linguagem != null && !linguagem.isEmpty()) {
            requestBody += ", \"parameters\": { \"language\": \"" + linguagem + "\"}";
        }
        requestBody += "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String body = response.getBody();
            // Extrair summary_text do JSON de resposta (ajustar conforme o formato real)
            int start = body.indexOf("summary_text") + 15;
            int end = body.indexOf("\"", start);
            return body.substring(start, end);
        }

        return "Erro ao gerar resumo com Hugging Face.";
    }
}
