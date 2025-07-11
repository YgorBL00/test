package com.example.newprojectbss.model;

import java.util.ArrayList;
import java.util.List;

public class LogicaUC_Evaporadora {

    public static class ResultadoUC_EV {
        public final double potenciaHP;
        public final int qtdVentiladores;

        public ResultadoUC_EV(double potenciaHP, int qtdVentiladores) {
            this.potenciaHP = potenciaHP;
            this.qtdVentiladores = qtdVentiladores;
        }
    }

    public static ResultadoUC_EV calcular(double areaM2, String tipoCamara) {
        double potenciaInicial;
        double incremento;
        double maxHP = 5.0;

        if (tipoCamara.equalsIgnoreCase("Congelado")) {
            potenciaInicial = 1.5;
            incremento = 1.5;
        } else {
            potenciaInicial = 1.0;
            incremento = 1.0;
        }

        if (areaM2 <= 4.0) {
            return new ResultadoUC_EV(potenciaInicial, 1);
        }

        // Calcula quantas vezes a área dobrou a partir de 4m2
        int vezesDobrou = (int) Math.floor(Math.log(areaM2 / 4.0) / Math.log(2));

        // Potência = potência inicial + incremento * vezesDobrou
        double potencia = potenciaInicial + incremento * vezesDobrou;
        if (potencia > maxHP) potencia = maxHP;

        // Quantidade de ventiladores igual a quantidade de HP (arredondado pra cima)
        int qtdEV = (int) Math.ceil(potencia);

        return new ResultadoUC_EV(potencia, qtdEV);
    }

    public static List<Item> gerarItensUCeEV(double areaM2, String tipoCamara) {
        ResultadoUC_EV resultado = calcular(areaM2, tipoCamara);

        System.out.println("=== Resultado UC e EV ===");
        System.out.println("Tipo de Câmara: " + tipoCamara);
        System.out.println("Área: " + areaM2 + " m²");
        System.out.println("Potência UC (HP): " + resultado.potenciaHP);
        System.out.println("Quantidade EV (ventiladores): " + resultado.qtdVentiladores);

        List<Item> itens = new ArrayList<>();

        // Item UC (Unidade Condensadora)
        itens.add(new Item(
                "Unidade Condensadora (UC)",
                tipoCamara + " - " + String.format("%.1f", resultado.potenciaHP) + " HP",
                "un",
                1,
                resultado.potenciaHP * 1500 // preço exemplo por HP
        ));

        // Item EV (Evaporadora - ventiladores)
        int qtdMicros = resultado.qtdVentiladores;
        String modelo = qtdMicros + " micro" + (qtdMicros > 1 ? "s" : "");

        itens.add(new Item(
                "Ventilador Evaporadora (EV)",
                modelo,  // ex: "3 micros"
                "un",
                1,
                qtdMicros * 1000.0
        ));


        return itens;
    }

}

