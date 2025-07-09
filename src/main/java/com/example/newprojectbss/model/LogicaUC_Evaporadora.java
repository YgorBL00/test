package com.example.newprojectbss.model;

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
}

