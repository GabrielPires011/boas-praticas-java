package br.com.alura.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    public HttpResponse<String> disparaRequisicao(String uri, String contentType, String method, Object object) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var builder = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method(method, object == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(object.toString()));

        if (contentType != null) {
            builder.header("Content-Type", contentType);
        }

        var request = builder.build();


        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
