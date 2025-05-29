package com.resumopro.ai_resumopro.controller;

import com.resumopro.ai_resumopro.service.ResumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/resumo")
public class ResumoController {

    @Autowired
    private ResumoService resumoService;

    // Endpoint POST para resumir texto
   @PostMapping
public ResponseEntity<Map<String, String>> resumirTexto(@RequestBody Map<String, String> request) {
    String texto = request.get("texto");
    String resumo = resumoService.gerarResumo(texto);

    Map<String, String> response = new HashMap<>();
    response.put("resumo", resumo);

    return ResponseEntity.ok(response);
}
}
