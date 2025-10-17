package com.example.newprojectbss.model;

import javafx.beans.property.*;

public class Cliente {

    private final IntegerProperty id;
    private final StringProperty nome;
    private final StringProperty email;
    private final StringProperty telefone;

    public Cliente(int id, String nome, String email, String telefone) {
        this.id = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.email = new SimpleStringProperty(email);
        this.telefone = new SimpleStringProperty(telefone);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telefoneProperty() { return telefone; }
}
