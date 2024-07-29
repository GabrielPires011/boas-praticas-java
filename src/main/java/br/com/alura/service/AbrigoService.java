package br.com.alura.service;

import br.com.alura.client.HttpService;
import br.com.alura.domain.Abrigo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AbrigoService {

    private final HttpService httpService;

    public AbrigoService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void cadastrarAbrigo() throws IOException, InterruptedException {
        System.out.println("Digite o nome do abrigo:");
        var nome = new Scanner(System.in).nextLine();
        System.out.println("Digite o telefone do abrigo:");
        var telefone = new Scanner(System.in).nextLine();
        System.out.println("Digite o email do abrigo:");
        var email = new Scanner(System.in).nextLine();

        var abrigo = new Abrigo(nome, telefone, email);
        var uri = "http://localhost:8080/abrigos";

        var response = httpService.disparaRequisicao(uri, "application/json", "POST", abrigo);
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

    public void listarAbrigo() throws IOException, InterruptedException {
        var uri = "http://localhost:8080/abrigos";
        var responseBody = httpService.disparaRequisicao(uri, null, "GET", null).body();
        var readValue = new ObjectMapper().readValue(responseBody, Abrigo[].class);
        var abrigoList = Arrays.stream(readValue).toList();

        if (abrigoList.isEmpty()) {
            System.out.println("Não há abrigos cadastrados.");
            return;
        }

        mostrarAbrigo(abrigoList);
    }

    private void mostrarAbrigo(List<Abrigo> abrigoList) {
        System.out.println("Abrigos cadastrados:");
        abrigoList.forEach(abrigo -> System.out.println(
                abrigo.getId() + " - " +
                        abrigo.getNome()));
    }
}
