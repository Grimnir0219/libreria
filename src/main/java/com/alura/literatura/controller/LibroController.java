package com.alura.literatura.controller;

import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private GutendexService gutendexService;

    // Obtener todos los libros
    @GetMapping
    public List<Libro> obtenerLibros(){
        return libroRepository.findAll();
    }

    // Crer un nuevo libro
    @PostMapping
    public Libro crearLibro(@RequestBody Libro libro){
        return libroRepository.save(libro);
    }

    // Obteber un libro por ID
    @GetMapping("/{id}")
    public Libro obtenerLibroPorId(@PathVariable Long id){
        return libroRepository.findById(id).orElse(null);
    }

    // Eliminar un libro por ID
    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Long id){
        libroRepository.deleteById(id);
    }

    // buscar y guardar un Libro por titulo desde la API Gutendex
    @GetMapping("/buscar")
    public Libro buscarYGuardarLibro(@RequestParam String titulo){
        try {
            // Buscar Libro en API
            Libro libro = gutendexService.buscarLibroPorTitulo(titulo);

            // Guardar Libro en la base de datos
            libroRepository.save(libro);

            return libro;
        } catch (Exception e) {
            throw new RuntimeException("Error al encontrar el libro: "+ e.getMessage());
        }
    }

    //Encontrar libros por idioma
    @GetMapping("/idioma")
    public List<Libro> obtenerLibrosPorIdioma(@RequestParam String idioma){
        return libroRepository.findByIdioma(idioma);
    }

    // Calcular total y promedio de descargas
    @GetMapping("/estadisticas/descargas")
    public String obtenerEstadisticasDeDescargas(){
        List<Libro> libros = libroRepository.findAll();

        //Calcula total y promedio de descarggas
        int totalDescargas = libros.stream().mapToInt(Libro::getDescargas).sum();
        double promedioDescargas = libros.stream().mapToInt(Libro::getDescargas).average().orElse(0);

        return "Total de descargas: " + totalDescargas + ", promedio de descargas: " +promedioDescargas;
    }
}
