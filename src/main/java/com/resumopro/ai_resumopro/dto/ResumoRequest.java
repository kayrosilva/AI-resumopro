package com.resumopro.ai_resumopro.dto;

public class ResumoRequest {
    private String texto;
    private String modelo;    // ex: "facebook/bart-large-cnn"
    private String linguagem; // ex: "pt", "en"

    // getters e setters

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getLinguagem() {
        return linguagem;
    }

    public void setLinguagem(String linguagem) {
        this.linguagem = linguagem;
    }
}
