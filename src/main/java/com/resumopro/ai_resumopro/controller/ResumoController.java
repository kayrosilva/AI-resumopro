package com.resumopro.ai_resumopro.controller;

import com.resumopro.ai_resumopro.dto.ResumoRequest;
import com.resumopro.ai_resumopro.dto.ResumoResponse;
import com.resumopro.ai_resumopro.service.ResumoServiceHuggingFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    @Autowired
    private ResumoServiceHuggingFace resumoService;

    @PostMapping
    public ResumoResponse gerarResumo(@RequestBody ResumoRequest request) {
        // Define um modelo default se não vier
        String modelo = request.getModelo() != null ? request.getModelo() : "facebook/bart-large-cnn";
        String linguagem = request.getLinguagem() != null ? request.getLinguagem() : "pt";

        String resumo = resumoService.gerarResumo(request.getTexto(), modelo, linguagem);
        return new ResumoResponse(resumo);
    }
}
