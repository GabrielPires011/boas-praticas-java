package br.com.alura.service;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    public static HttpResponse<String> disparaRequisicao(String uri, String contentType, String method, JsonObject json) throws IOException, InterruptedException {
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
