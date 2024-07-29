package br.com.alura.service;

import br.com.alura.client.HttpService;
import br.com.alura.domain.Abrigo;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbrigoServiceTest {
    private final HttpService client = mock(HttpService.class);
    private final AbrigoService abrigoService = new AbrigoService(client);
    private final HttpResponse<String> response = mock(HttpResponse.class);
    private final Abrigo abrigo = new Abrigo("Teste", "6181880392", "abrigo_alura@gmail.com");

    @Test
    public void deveVerificarSeDispararRequisicaoGetSeraChamado() throws IOException, InterruptedException {
        abrigo.setId(0L);
        abrigo.setNome("Teste");

        String expectedAbrigosCadastrados = "Abrigos cadastrados:";
        String expectedIdENome = "0 - Teste";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[{"+ abrigo +"}]");
        when(client.disparaRequisicao(anyString(), isNull(), anyString(), isNull())).thenReturn(response);

        abrigoService.listarAbrigo();

        System.setOut(System.out);

        String[] lines = baos.toString().split(System.lineSeparator());
        assertEquals(expectedAbrigosCadastrados, lines[0].trim());
        assertEquals(expectedIdENome, lines[1].trim());
    }
}
