package br.com.alura.service;

import br.com.alura.client.HttpService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PetsService {

    private final HttpService httpService;

    public PetsService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void importarPetsAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        var idOuNome = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        var nomeArquivo = new Scanner(System.in).nextLine();

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

            var json = new JsonObject();
            json.addProperty("tipo", tipo.toUpperCase());
            json.addProperty("nome", nome);
            json.addProperty("raca", raca);
            json.addProperty("idade", idade);
            json.addProperty("cor", cor);
            json.addProperty("peso", peso);

            var uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";

            var response = httpService.disparaRequisicao(uri, "application/json", "POST", json);
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

    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        var idOuNome = new Scanner(System.in).nextLine();

        var uri = "http://localhost:8080/abrigos/" +idOuNome +"/pets";

        var response = httpService.disparaRequisicao(uri, null, "GET", null);
        var statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID ou nome não cadastrado!");
            return;
        }
        var responseBody = response.body();
        var jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Pets cadastrados:");
        for (var element : jsonArray) {
            var jsonObject = element.getAsJsonObject();
            var id = jsonObject.get("id").getAsLong();
            var tipo = jsonObject.get("tipo").getAsString();
            var nome = jsonObject.get("nome").getAsString();
            var raca = jsonObject.get("raca").getAsString();
            var idade = jsonObject.get("idade").getAsInt();
            System.out.println(id +" - " +tipo +" - " +nome +" - " +raca +" - " +idade +" ano(s)");
        }
    }
}
