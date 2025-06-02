package com.resumopro.ai_resumopro.dto;

public class ResumoResponse {
    private String resumo;

    public ResumoResponse(String resumo) {
        this.resumo = resumo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }
}
