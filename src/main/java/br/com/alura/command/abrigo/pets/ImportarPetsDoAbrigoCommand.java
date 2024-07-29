package br.com.alura.command.abrigo.pets;

import br.com.alura.client.HttpService;
import br.com.alura.command.Command;
import br.com.alura.service.PetsService;

import java.io.IOException;
import java.util.Scanner;

public class ImportarPetsDoAbrigoCommand implements Command {
    @Override
    public void execute() {
        try {
            var httpService = new HttpService();
            var petsService = new PetsService(httpService);

            System.out.println("Digite o id ou nome do abrigo:");
            var idOuNome = new Scanner(System.in).nextLine();

            System.out.println("Digite o nome do arquivo CSV:");
            var nomeArquivo = new Scanner(System.in).nextLine();

            petsService.importarPetsAbrigo(idOuNome, nomeArquivo);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
