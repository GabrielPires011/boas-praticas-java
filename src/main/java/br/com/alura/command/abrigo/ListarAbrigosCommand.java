package br.com.alura.command.abrigo;

import br.com.alura.client.HttpService;
import br.com.alura.command.Command;
import br.com.alura.service.AbrigoService;

import java.io.IOException;

public class ListarAbrigosCommand implements Command {
    @Override
    public void execute() {
        try {
            var httpService = new HttpService();
            var abrigoService = new AbrigoService(httpService);

            abrigoService.listarAbrigo();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
