package com.imc.Util;

import com.imc.Model.Pessoa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {
    private static final String NOME_ARQUIVO = "dados_pessoas.txt";

    /**
     * Salva uma lista de pessoas no arquivo
     */
    public static void salvarPessoas(List<Pessoa> pessoas) throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            for (Pessoa pessoa : pessoas) {
                writer.write(pessoa.toCSV() + "\n");
            }
        }
    }

    /**
     * Carrega a lista de pessoas do arquivo
     */
    public static List<Pessoa> carregarPessoas() throws IOException {
        List<Pessoa> pessoas = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);

        // Verifica se o arquivo existe
        if (!arquivo.exists()) {
            return pessoas; // Retorna lista vazia se arquivo não existe
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    try {
                        Pessoa pessoa = Pessoa.fromCSV(linha);
                        pessoas.add(pessoa);
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha: " + linha);
                        e.printStackTrace();
                    }
                }
            }
        }

        return pessoas;
    }

    /**
     * Adiciona uma nova pessoa ao arquivo (append)
     */
    public static void adicionarPessoa(Pessoa pessoa) throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO, true)) {
            writer.write(pessoa.toCSV() + "\n");
        }
    }
}
