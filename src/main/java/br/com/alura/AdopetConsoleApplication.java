package br.com.alura;

import java.util.Scanner;

import static br.com.alura.service.AbrigoService.*;

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
}
