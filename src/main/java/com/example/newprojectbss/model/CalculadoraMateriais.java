package com.example.newprojectbss.model;

public class CalculadoraMateriais {

    private int totalPaineis;
    private int paineisParedes;
    private int paineisTeto;
    private int paineisPiso;

    private int cantoneirasInternas;
    private int cantoneirasExternas;

    private int perfisU;
    private final int perfisUPorta = 2; // fixo

    private final double larguraPainel = 1.15; // em metros (fixo)

    // Preços unitários (você pode ajustar)
    private double precoPainel;
    private final double precoCantoneira = 30; // ex: 30 reais por unidade
    private final double precoPerfilU = 40; // ex: 40 reais por unidade

    private double custoTotal;

    public void calcular() {
        DadosCâmara dados = DadosCâmara.getInstancia();

        double comprimento = dados.getComprimento();
        double largura = dados.getLargura();
        double altura = dados.getAltura();
        boolean temPiso = dados.isTemPiso();
        String tipoCamara = dados.getTipoCamara();

        // Ajusta o preço do painel conforme tipo da câmara
        if (tipoCamara.equalsIgnoreCase("Congelado")) {
            precoPainel = 350; // preço por painel congelada
        } else {
            precoPainel = 250; // preço por painel resfriada
        }

        // --- Cálculo painéis ---

        double perimetro = 2 * (comprimento + largura);

        // Paredes: qtd painéis arredondando pra cima
        paineisParedes = (int) Math.ceil(perimetro / larguraPainel);

        // Teto: área do teto / área do painel (arredondar pra cima)
        double areaTeto = comprimento * largura;
        double alturaTeto = Math.min(comprimento, largura);
        int paineisTetoCalc = (int) Math.ceil(areaTeto / (alturaTeto * larguraPainel));
        paineisTeto = paineisTetoCalc;

        // Piso (se tem piso)
        if (temPiso) {
            int paineisPisoCalc = (int) Math.ceil(areaTeto / (alturaTeto * larguraPainel));
            paineisPiso = paineisPisoCalc;
        } else {
            paineisPiso = 0;
        }

        // Total painéis
        totalPaineis = paineisParedes + paineisTeto + paineisPiso;

        // --- Cálculo Cantoneiras ---
        cantoneirasInternas = (int) Math.ceil(perimetro / 2);
        cantoneirasExternas = (int) Math.ceil(perimetro / 2);

        // --- Perfil U ---
        perfisU = (int) Math.ceil(perimetro / 3);
        perfisU += perfisUPorta;

        // Calcula custo total
        calcularCustoTotal();
    }

    private void calcularCustoTotal() {
        // Soma os custos: painéis + cantoneiras internas + cantoneiras externas + perfis U
        double custoPaineis = totalPaineis * precoPainel;
        double custoCantoneiras = (cantoneirasInternas + cantoneirasExternas) * precoCantoneira;
        double custoPerfisU = perfisU * precoPerfilU;

        custoTotal = custoPaineis + custoCantoneiras + custoPerfisU;
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public void validar() {
        System.out.println("== Cálculo Materiais ==");
        System.out.println("Painéis paredes: " + paineisParedes);
        System.out.println("Painéis teto: " + paineisTeto);
        System.out.println("Painéis piso: " + paineisPiso);
        System.out.println("Total painéis: " + totalPaineis);

        System.out.println("Cantoneiras internas: " + cantoneirasInternas);
        System.out.println("Cantoneiras externas: " + cantoneirasExternas);

        System.out.println("Perfis U (total, incluindo porta): " + perfisU);
        System.out.println("Perfis U porta (fixo): " + perfisUPorta);

        System.out.printf("Custo total estimado: R$ %.2f\n", custoTotal);
    }

    // Getters caso queira usar externamente
    public int getTotalPaineis() { return totalPaineis; }
    public int getPaineisParedes() { return paineisParedes; }
    public int getPaineisTeto() { return paineisTeto; }
    public int getPaineisPiso() { return paineisPiso; }
    public int getCantoneirasInternas() { return cantoneirasInternas; }
    public int getCantoneirasExternas() { return cantoneirasExternas; }
    public int getPerfisU() { return perfisU; }
}
