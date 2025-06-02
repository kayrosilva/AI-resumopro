package com.resumopro.ai_resumopro.service;

import com.resumopro.ai_resumopro.client.HuggingFaceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumoServiceHuggingFace {

    @Autowired
    private HuggingFaceClient huggingFaceClient;

    public String gerarResumo(String texto, String modelo, String linguagem) {
        return huggingFaceClient.resumirTexto(texto, modelo, linguagem);
    }
}
