package br.com.alura;

import br.com.alura.client.HttpService;

import br.com.alura.service.PetsService;

import java.io.IOException;
import java.util.Scanner;

public class ListarPetsDoAbrigoCommand implements Command{
    @Override
    public void execute() {
        try {
            var httpService = new HttpService();
            var petsService = new PetsService(httpService);

            System.out.println("Digite o id ou nome do abrigo:");
            var idOuNome = new Scanner(System.in).nextLine();
            petsService.listarPetsDoAbrigo(idOuNome);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}