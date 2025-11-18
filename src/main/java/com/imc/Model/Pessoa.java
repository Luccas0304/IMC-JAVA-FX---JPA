package com.imc.Model;

public class Pessoa {
    private String nome;
    private double altura;
    private double peso;
    private double imc;
    private String classificacao;

    public Pessoa(String nome, double altura, double peso) {
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
        calcularIMC();
    }

    private void calcularIMC() {
        if (altura > 0) {
            this.imc = peso / (altura * altura);
            this.classificacao = determinarClassificacao();
        }
    }

    private String determinarClassificacao() {
        if (imc < 18.5) {
            return "Abaixo do Peso";
        } else if (imc < 24.9) {
            return "Peso Normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade Grau 1";
        } else if (imc < 39.9) {
            return "Obesidade Grau 2";
        } else {
            return "Obesidade Grau 3";
        }
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public double getImc() {
        return imc;
    }

    public String getClassificacao() {
        return classificacao;
    }

    // Método para converter para formato CSV
    public String toCSV() {
        return String.format(java.util.Locale.US, "%s,%.2f,%.2f,%.2f,%s",
                nome, altura, peso, imc, classificacao);
    }

    // Método estático para criar Pessoa a partir de linha CSV
    public static Pessoa fromCSV(String linha) {
        String[] dados = linha.split(",");
        return new Pessoa(dados[0],
                Double.parseDouble(dados[1]),
                Double.parseDouble(dados[2]));
    }

    @Override
    public String toString() {
        return String.format("%s - Altura: %.2fm, Peso: %.2fkg, IMC: %.2f (%s)",
                nome, altura, peso, imc, classificacao);
    }
}