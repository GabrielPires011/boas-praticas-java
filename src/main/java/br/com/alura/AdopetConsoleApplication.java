package br.com.alura;

import br.com.alura.service.AbrigoService;
import br.com.alura.client.HttpService;
import br.com.alura.service.PetsService;

import java.util.Scanner;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        var httpService = new HttpService();
        var abrigoService = new AbrigoService(httpService);
        var petsService = new PetsService(httpService);
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
                    abrigoService.listarAbrigo();
                } else if (opcaoEscolhida == 2) {
                    abrigoService.cadastrarAbrigo();
                } else if (opcaoEscolhida == 3) {
                    System.out.println("Digite o id ou nome do abrigo:");
                    var idOuNome = new Scanner(System.in).nextLine();
                    petsService.listarPetsDoAbrigo(idOuNome);
                } else if (opcaoEscolhida == 4) {
                    System.out.println("Digite o id ou nome do abrigo:");
                    var idOuNome = new Scanner(System.in).nextLine();

                    System.out.println("Digite o nome do arquivo CSV:");
                    var nomeArquivo = new Scanner(System.in).nextLine();
                    petsService.importarPetsAbrigo(idOuNome, nomeArquivo);
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
}
