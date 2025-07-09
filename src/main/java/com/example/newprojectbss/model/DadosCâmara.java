package com.example.newprojectbss.model;

public class DadosCâmara {

    private static DadosCâmara instancia = null;

    private String tipoCamara;    // "Resfriado" ou "Congelado"
    private String tipoPorta;     // "Giratoria" ou "Correr"
    private static double comprimento;
    private static double largura;
    private static double altura;
    private boolean temPiso;

    // Construtor privado para singleton
    private DadosCâmara() {}

    // Método para obter a instância
    public static DadosCâmara getInstancia() {
        if (instancia == null) {
            instancia = new DadosCâmara();
        }
        return instancia;
    }

    // Getters e setters
    public String getTipoCamara() {
        return tipoCamara;
    }

    public void setTipoCamara(String tipoCamara) {
        this.tipoCamara = tipoCamara;
    }

    public String getTipoPorta() {
        return tipoPorta;
    }

    public void setTipoPorta(String tipoPorta) {
        this.tipoPorta = tipoPorta;
    }

    public static double getComprimento() {
        return comprimento;
    }

    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    public static double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public static double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public boolean isTemPiso() {
        return temPiso;
    }

    public void setTemPiso(boolean temPiso) {
        this.temPiso = temPiso;
    }
}
