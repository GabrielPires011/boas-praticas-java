package br.com.alura;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        System.out.println("##### BOAS VINDAS AO SISTEMA ADOPET CONSOLE #####");
        try {
            var opcaoEscolhida = 0;
            while (opcaoEscolhida != 5) {
                System.out.println("\nDIGITE O NÚMERO DA OPERAÇÃO DESEJADA:");
                System.out.println("1 -> Listar abrigos cadastrados");
                System.out.println("2 -> Cadastrar novo abrigo");
                System.out.println("3 -> Listar pets do abrigo");
                System.out.println("4 -> Importar pets do abrigo");
                System.out.println("5 -> Sair");

                var textoDigitado = new Scanner(System.in).nextLine();
                opcaoEscolhida = Integer.parseInt(textoDigitado);

                if (opcaoEscolhida == 1) {
                    listarAbrigo();
                } else if (opcaoEscolhida == 2) {
                    cadastrarAbrigo();
                } else if (opcaoEscolhida == 3) {
                    listarPetsDoAbrigo();
                } else if (opcaoEscolhida == 4) {
                    importarPetsAbrigo();
                } else if (opcaoEscolhida == 5) {
                    break;
                } else {
                    System.out.println("NÚMERO INVÁLIDO!");
                    opcaoEscolhida = 0;
                }
            }
            System.out.println("Finalizando o programa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void importarPetsAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        var idOuNome = new Scanner(System.in).nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        var nomeArquivo = new Scanner(System.in).nextLine();

        BufferedReader reader = null;
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

            var response = disparaRequisicao(uri, "application/json", "POST", json);
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

    private static void listarPetsDoAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o id ou nome do abrigo:");
        var idOuNome = new Scanner(System.in).nextLine();

        var uri = "http://localhost:8080/abrigos/" +idOuNome +"/pets";

        var response = disparaRequisicao(uri, null, "GET", null);
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

    private static void cadastrarAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o nome do abrigo:");
        var nome = new Scanner(System.in).nextLine();
        System.out.println("Digite o telefone do abrigo:");
        var telefone = new Scanner(System.in).nextLine();
        System.out.println("Digite o email do abrigo:");
        var email = new Scanner(System.in).nextLine();

        var json = new JsonObject();
        json.addProperty("nome", nome);
        json.addProperty("telefone", telefone);
        json.addProperty("email", email);

        var uri = "http://localhost:8080/abrigos";

        var response = disparaRequisicao(uri, "application/json", "POST", json);
        var statusCode = response.statusCode();
        var responseBody = response.body();
        if (statusCode == 200) {
            System.out.println("Abrigo cadastrado com sucesso!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Erro ao cadastrar o abrigo:");
            System.out.println(responseBody);
        }
    }

    private static void listarAbrigo() throws IOException, InterruptedException {
        var uri = "http://localhost:8080/abrigos";
        var responseBody = disparaRequisicao(uri, null, "GET", null).body();
        var jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
        System.out.println("Abrigos cadastrados:");
        for (var element : jsonArray) {
            var jsonObject = element.getAsJsonObject();
            var id = jsonObject.get("id").getAsLong();
            var nome = jsonObject.get("nome").getAsString();
            System.out.println(id +" - " +nome);
        }
    }

    private static HttpResponse<String> disparaRequisicao(String uri, String contentType, String method, JsonObject json) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method(method, json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json.toString()));

        if (contentType != null) {
            builder.header("Content-Type", contentType);
        }

        var request = builder.build();


        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
