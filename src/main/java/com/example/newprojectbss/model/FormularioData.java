package com.example.newprojectbss.model;

public class FormularioData {

    private String tipoCamara;    // "Resfriado" ou "Congelado"
    private boolean possuiPiso;
    private String tipoPorta;     // "Giratoria" ou "De correr"

    private double comprimento;
    private double largura;
    private double altura;

    // Getters e setters
    public String getTipoCamara() { return tipoCamara; }
    public void setTipoCamara(String tipoCamara) { this.tipoCamara = tipoCamara; }

    public boolean isPossuiPiso() { return possuiPiso; }
    public void setPossuiPiso(boolean possuiPiso) { this.possuiPiso = possuiPiso; }

    public String getTipoPorta() { return tipoPorta; }
    public void setTipoPorta(String tipoPorta) { this.tipoPorta = tipoPorta; }

    public double getComprimento() { return comprimento; }
    public void setComprimento(double comprimento) { this.comprimento = comprimento; }

    public double getLargura() { return largura; }
    public void setLargura(double largura) { this.largura = largura; }

    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
}
