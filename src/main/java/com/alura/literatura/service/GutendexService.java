package com.alura.literatura.service;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GutendexService {

    private static final String GUTENDEX_API_URL = "https://gutendex.com/books/";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    // Buscar libros por palabra clave con soporte para paginación
    public List<Libro> buscarLibrosPorPalabra(String palabra, int limite) {
        List<Libro> libros = new ArrayList<>();
        String url = GUTENDEX_API_URL + "?search=" + palabra.replace(" ", "+");

        try {
            RestTemplate restTemplate = new RestTemplate();

            while (url != null && libros.size() < limite) {
                Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);
                List<Map<String, Object>> resultados = (List<Map<String, Object>>) respuesta.get("results");

                for (Map<String, Object> libroData : resultados) {
                    if (libros.size() >= limite) break;

                    String titulo = (String) libroData.get("title");
                    List<Map<String, Object>> autoresData = (List<Map<String, Object>>) libroData.get("authors");

                    Autor autor = null;
                    if (autoresData != null && !autoresData.isEmpty()) {
                        String nombreAutor = (String) autoresData.get(0).get("name");
                        Integer birthYear = (autoresData.get(0).get("birth_year") != null) ? (Integer) autoresData.get(0).get("birth_year") : null;
                        Integer deathYear = (autoresData.get(0).get("death_year") != null) ? (Integer) autoresData.get(0).get("death_year") : null;
                        autor = new Autor(nombreAutor, birthYear, deathYear);
                    }

                    List<String> idiomas = (List<String>) libroData.get("languages");
                    String idioma = idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "Desconocido";
                    int descargas = (int) libroData.get("download_count");

                    libros.add(new Libro(titulo, idioma, descargas, autor));
                }

                url = (String) respuesta.get("next"); // Obtener la siguiente página
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros en la API de Gutendex: " + e.getMessage());
        }

        return libros;
    }

    // Buscar libro específico por título
    public Libro buscarLibroPorTitulo(String titulo) {
        try {
            String url = GUTENDEX_API_URL + "?search=" + titulo.replace(" ", "+");
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> respuesta = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> resultados = (List<Map<String, Object>>) respuesta.get("results");
            if (resultados.isEmpty()) {
                throw new RuntimeException("El libro no fue encontrado en la API de Gutendex.");
            }

            Map<String, Object> libroData = resultados.get(0);

            String tituloLibro = (String) libroData.get("title");
            List<Map<String, Object>> autoresData = (List<Map<String, Object>>) libroData.get("authors");

            Autor autor = null;
            if (autoresData != null && !autoresData.isEmpty()) {
                String nombreAutor = (String) autoresData.get(0).get("name");
                Integer birthYear = (autoresData.get(0).get("birth_year") != null) ? (Integer) autoresData.get(0).get("birth_year") : null;
                Integer deathYear = (autoresData.get(0).get("death_year") != null) ? (Integer) autoresData.get(0).get("death_year") : null;
                autor = new Autor(nombreAutor, birthYear, deathYear);
            }

            List<String> idiomas = (List<String>) libroData.get("languages");
            String idioma = idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "Desconocido";
            int descargas = (int) libroData.get("download_count");

            return new Libro(tituloLibro, idioma, descargas, autor);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el libro \"" + titulo + "\" en la API de Gutendex: " + e.getMessage());
        }
    }

    // Buscar y registrar un libro por título
    public Libro buscarYRegistrarLibroEnAPI(String titulo) throws Exception {
        // Realizar la búsqueda del libro en la API
        Libro libro = buscarLibroPorTitulo(titulo);

        if (libro == null) {
            throw new Exception("No se encontró el libro con el título: " + titulo);
        }

        // Verificar si el autor ya existe en la base de datos
        List<Autor> autoresEncontrados = autorRepository.findByNombre(libro.getAutor().getNombre());
        Autor autorExistente = autoresEncontrados.isEmpty() ? null : autoresEncontrados.get(0);

        if (autorExistente != null) {
            libro.setAutor(autorExistente); // Asociar el autor existente
        } else {
            // Guardar el nuevo autor en la base de datos
            Autor nuevoAutor = libro.getAutor();
            autorRepository.save(nuevoAutor);
            libro.setAutor(nuevoAutor);
        }

        // Guardar el libro en la base de datos
        libroRepository.save(libro);

        return libro;
    }
}
