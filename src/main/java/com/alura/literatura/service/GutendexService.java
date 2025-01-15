package com.alura.literatura.service;

import com.alura.literatura.model.Libro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@Service
public class GutendexService {

    private final HttpClient client = HttpClient.newHttpClient();

    public Libro buscarLibroPorTitulo(String titulo) throws Exception {

        // Construir la URL para la consulta
        String url = "https://gutendex.com/books/?search=" + titulo;

        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Enviar la solicitud y obtener la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Analizar la respuesta JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        // Tomar el primer libro del resultado
        JsonNode libroNode = root.get("results").get(0);

        // Mapear los datos al objeto Libro
        Libro libro = new Libro();
        libro.setTitulo(libroNode.get("title").asText());
        libro.setIdioma(libroNode.get("languages").get(0).asText());
        libro.setDescargas(libroNode.get("download_count").asInt());

        return libro;
    }
}
