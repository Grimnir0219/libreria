package com.alura.literatura.controller;

import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private GutendexService gutendexService;

    // Crear un nuevo libro
    @PostMapping
    public String crearLibro(@RequestBody Libro libro) {
        System.out.println("Intentando registrar el libro: " + libro.getTitulo());

        // Verificar si el libro ya existe
        List<Libro> existentes = libroRepository.findByTitulo(libro.getTitulo());
        if (!existentes.isEmpty()) {
            String mensaje = "No se puede registrar el libro \"" + libro.getTitulo() + "\" más de una vez.";
            System.out.println(mensaje); // Mostrar mensaje en la consola
            return mensaje;
        }

        // Guardar el libro si no existe
        libroRepository.save(libro);
        String mensajeExito = "Libro registrado exitosamente: " + libro;
        System.out.println(mensajeExito); // Mostrar mensaje en la consola
        return mensajeExito;
    }

    // Obtener todos los libros
    @GetMapping
    public List<Libro> obtenerLibros() {
        return libroRepository.findAll();
    }

    // Obtener un libro por ID
    @GetMapping("/{id}")
    public Libro obtenerLibroPorId(@PathVariable Long id) {
        return libroRepository.findById(id).orElse(null);
    }

    // Eliminar un libro por ID
    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Long id) {
        libroRepository.deleteById(id);
    }

    // Buscar y guardar un libro por título desde la API Gutendex
    @GetMapping("/buscar")
    public String buscarYGuardarLibro(@RequestParam String titulo) {
        System.out.println("Buscando y registrando libro: " + titulo);

        // Verificar si el libro ya existe
        List<Libro> existentes = libroRepository.findByTitulo(titulo);
        if (!existentes.isEmpty()) {
            String mensaje = "No se puede registrar el libro \"" + titulo + "\" más de una vez.";
            System.out.println(mensaje); // Mostrar mensaje en la consola
            return mensaje;
        }

        // Buscar el libro en la API de Gutendex y guardarlo
        Libro libro = gutendexService.buscarLibroPorTitulo(titulo);
        libroRepository.save(libro);
        String mensajeExito = "Libro registrado exitosamente: " + libro;
        System.out.println(mensajeExito); // Mostrar mensaje en la consola
        return mensajeExito;
    }

    // Encontrar libros por idioma
    @GetMapping("/idioma")
    public List<Libro> obtenerLibrosPorIdioma(@RequestParam String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    // Calcular total y promedio de descargas
    @GetMapping("/estadisticas/descargas")
    public String obtenerEstadisticasDeDescargas() {
        List<Libro> libros = libroRepository.findAll();

        // Calcular total y promedio de descargas
        int totalDescargas = libros.stream().mapToInt(Libro::getDescargas).sum();
        double promedioDescargas = libros.stream().mapToInt(Libro::getDescargas).average().orElse(0);

        return "Total de descargas: " + totalDescargas + ", promedio de descargas: " + promedioDescargas;
    }

    // Estadísticas avanzadas de descargas
    @GetMapping("/estadisticas/avanzadas")
    public DoubleSummaryStatistics obtenerEstadisticasAvanzadas() {
        List<Libro> libros = libroRepository.findAll();

        return libros.stream()
                .mapToDouble(Libro::getDescargas)
                .summaryStatistics();
    }
}

