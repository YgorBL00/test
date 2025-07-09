package com.example.newprojectbss.model;

public class Item {
    private final String nome;
    private final String modelo;
    private final String unidade;
    private final int quantidade;
    private final double valor;

    public Item(String nome, String modelo, String unidade, int quantidade, double valor) {
        this.nome = nome;
        this.modelo = modelo;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public String getModelo() {
        return modelo;
    }

    public String getUnidade() {
        return unidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }
}
