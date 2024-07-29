package br.com.alura.service;

import br.com.alura.client.HttpService;
import br.com.alura.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class PetsService {

    private final HttpService httpService;

    public PetsService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void importarPetsAbrigo(String idOuNome, String nomeArquivo) throws IOException, InterruptedException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " +nomeArquivo);
            return;
        }
        String line;
        while ((line = reader.readLine()) != null) {
            var campos = line.split(",");
            var tipo = campos[0];
            var nome = campos[1];
            var raca = campos[2];
            var idade = Integer.parseInt(campos[3]);
            var cor = campos[4];
            var peso = Float.parseFloat(campos[5]);

            var pet = new Pet(tipo, nome, raca, idade, cor, peso);

            var uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";

            var response = httpService.disparaRequisicao(uri, "application/json", "POST", pet);
            var statusCode = response.statusCode();
            var responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Pet cadastrado com sucesso: " + nome);
            } else if (statusCode == 404) {
                System.out.println("Id ou nome do abrigo não encontado!");
                break;
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Erro ao cadastrar o pet: " + nome);
                System.out.println(responseBody);
                break;
            }
        }
        reader.close();
    }

    public void listarPetsDoAbrigo(String idOuNome) throws IOException, InterruptedException {
        if (idOuNome == null || idOuNome.isEmpty()) {
            System.out.println("ID ou Nome não fornecido. Por favor, forneça um valor válido.");
            return;
        }

        var uri = "http://localhost:8080/abrigos/" +idOuNome +"/pets";

        var response = httpService.disparaRequisicao(uri, null, "GET", null);
        var statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID ou nome não cadastrado!");
            return;
        }
        var responseBody = response.body();
        var readValue = new ObjectMapper().readValue(responseBody, Pet[].class);
        System.out.println("Pets cadastrados:");
        Arrays.stream(readValue).toList().forEach(pet -> System.out.println(
                pet.getId() + " - " +
                pet.getTipo() + " - " +
                pet.getNome() + " - " +
                pet.getRaca() + " - " +
                pet.getIdade() + " ano(s)"));
    }
}
