package com.alura.literatura.service;

import com.alura.literatura.model.Libro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GutendexService {

    private static final String BASE_URL = "https://gutendex.com/books/";

    public Libro buscarLibroPorTitulo(String titulo) {
        try {
            // Codificar el título para que sea válido en la URL
            String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            String url = BASE_URL + "?search=" + tituloCodificado;

            // Realizar la solicitud HTTP
            RestTemplate restTemplate = new RestTemplate();
            String respuestaJson = restTemplate.getForObject(url, String.class);

            // Parsear el JSON de la respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(respuestaJson);

            // Verificar si hay resultados
            JsonNode resultados = root.get("results");
            if (resultados == null || resultados.isEmpty()) {
                throw new RuntimeException("No se encontraron libros con el título proporcionado.");
            }

            // Extraer el primer libro de la lista de resultados
            JsonNode libroNode = resultados.get(0);
            Libro libro = new Libro();
            libro.setTitulo(libroNode.get("title").asText());
            libro.setIdioma(libroNode.get("languages").get(0).asText());
            libro.setDescargas(libroNode.get("download_count").asInt());

            return libro;

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el libro: " + e.getMessage());
        }
    }
}
