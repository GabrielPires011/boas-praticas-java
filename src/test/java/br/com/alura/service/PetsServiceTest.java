package br.com.alura.service;

import br.com.alura.client.HttpService;
import br.com.alura.domain.Pet;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PetsServiceTest {
    private final HttpService client = mock(HttpService.class);
    private final PetsService petsService = new PetsService(client);
    private final HttpResponse<String> response = mock(HttpResponse.class);
    private final Pet pet = new Pet(0L, "Cachorro", "Lua", "Dálmata", 1, "Preto", 10.00F);

    @Test
    public void deveVerificarSeDispararRequisicaoGetSeraChamado() throws IOException, InterruptedException {

        String expectedAbrigosCadastrados = "Pets cadastrados:";
        String expectedIdENome = "0 - Cachorro - Lua - Dálmata - 1 ano(s)";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[{"+ pet +"}]");
        when(client.disparaRequisicao(anyString(), isNull(), anyString(), isNull())).thenReturn(response);

        petsService.listarPetsDoAbrigo("0");

        System.setOut(System.out);

        String[] lines = baos.toString().split(System.lineSeparator());
        assertEquals(expectedAbrigosCadastrados, lines[0].trim());
        assertEquals(expectedIdENome, lines[1].trim());
    }

    @Test
    public void deveVerificarQuandoNaoHaIdOuNomeCadastrado() throws IOException, InterruptedException {
        String expected = "ID ou nome não cadastrado!";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.statusCode()).thenReturn(404);
        when(client.disparaRequisicao(anyString(), isNull(), anyString(), isNull())).thenReturn(response);

        petsService.listarPetsDoAbrigo("1");

        System.setOut(System.out);

        String[] lines = baos.toString().split(System.lineSeparator());
        assertEquals(expected, lines[0].trim());
    }

    @Test
    public void deveVerificarQuandoIdOuNomeVemNull() throws IOException, InterruptedException {
        String expected = "ID ou Nome não fornecido. Por favor, forneça um valor válido.";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        petsService.listarPetsDoAbrigo(null);

        System.setOut(System.out);

        String[] lines = baos.toString().split(System.lineSeparator());
        assertEquals(expected, lines[0].trim());
    }

    @Test
    public void deveVerificarQuandoIdOuNomeVemVazio() throws IOException, InterruptedException {
        String expected = "ID ou Nome não fornecido. Por favor, forneça um valor válido.";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        petsService.listarPetsDoAbrigo("");

        System.setOut(System.out);

        String[] lines = baos.toString().split(System.lineSeparator());
        assertEquals(expected, lines[0].trim());
    }

    @Test
    public void deveVerificarSeDispararRequisicaoPostSeraChamado() throws IOException, InterruptedException {
        when(client.disparaRequisicao(anyString(), anyString(), anyString(), any())).thenReturn(response);

        petsService.importarPetsAbrigo("0","pets.csv");

        verify(client.disparaRequisicao(anyString(), anyString(), anyString(), any()), atLeast(1));
    }
}
