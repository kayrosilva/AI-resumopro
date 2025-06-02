package com.resumopro.ai_resumopro.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Component
public class HuggingFaceClient {

    @Value("${huggingface.api.token}")
    private String huggingfaceToken;

    private final RestTemplate restTemplate;

    public HuggingFaceClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 5 segundos para abrir conexão
        factory.setReadTimeout(30000);    // 30 segundos para aguardar resposta
        this.restTemplate = new RestTemplate(factory);
    }

    public String resumirTexto(String textoOriginal, String modelo, String linguagem) {
        String url = "https://api-inference.huggingface.co/models/" + modelo;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(huggingfaceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Montar JSON da requisição com escape para aspas no texto
        String requestBody = "{\"inputs\": \"" + textoOriginal.replace("\"", "\\\"") + "\"";

        if (linguagem != null && !linguagem.isEmpty()) {
            requestBody += ", \"parameters\": { \"language\": \"" + linguagem + "\"}";
        }
        requestBody += "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                // A resposta normalmente é um array com objetos que contêm "summary_text"
                if (root.isArray() && root.size() > 0) {
                    JsonNode first = root.get(0);
                    if (first.has("summary_text")) {
                        return first.get("summary_text").asText();
                    }
                }

                return "Resumo não encontrado na resposta da API.";
            } else {
                return "Erro: resposta não OK do Hugging Face - código " + response.getStatusCodeValue();
            }

        } catch (HttpServerErrorException.GatewayTimeout e) {
            return "Tempo de espera esgotado ao processar o resumo (504 Gateway Timeout). Tente novamente mais tarde.";
        } catch (ResourceAccessException e) {
            return "Erro de rede ao tentar acessar Hugging Face. Verifique sua conexão.";
        } catch (Exception e) {
            return "Erro inesperado ao gerar resumo: " + e.getMessage();
        }
    }
}
