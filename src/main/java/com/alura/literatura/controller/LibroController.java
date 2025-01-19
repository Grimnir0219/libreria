package com.alura.literatura.controller;

import com.alura.literatura.exception.LibroDuplicadoException;
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

    // Crear un nuevo libro
    @PostMapping
    public void crearLibro(@RequestBody Libro libro) {
        System.out.println("Intentando registrar el libro: " + libro.getTitulo());

        // Verificar si el libro ya existe
        boolean existe = !libroRepository.findByTitulo(libro.getTitulo()).isEmpty();
        if (existe) {
            // Lanzar excepción personalizada
            throw new LibroDuplicadoException("No se puede registrar el libro \"" + libro.getTitulo() + "\" más de una vez.");
        }

        // Guardar el libro si no existe
        libroRepository.save(libro);
        System.out.println("Libro registrado exitosamente: " + libro);
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

//    // Eliminar un libro por ID
//    @DeleteMapping("/{id}")
//    public void eliminarLibro(@PathVariable Long id) {
//        libroRepository.deleteById(id);
//    }

    // Buscar y guardar un libro por título desde la API Gutendex
    @GetMapping("/buscar")
    public void buscarYGuardarLibro(@RequestParam String titulo) {
        System.out.println("Buscando y registrando libro: " + titulo);

        // Validación previa: Verificar si el libro ya existe
        List<Libro> existentes = libroRepository.findByTitulo(titulo);
        if (!existentes.isEmpty()) {
            System.out.println("No se puede registrar el libro \"" + titulo + "\" más de una vez.");
            return;
        }

        // Buscar en la API y guardar si no existe
        Libro libro = gutendexService.buscarLibroPorTitulo(titulo);
        libroRepository.save(libro);
        System.out.println("Libro registrado exitosamente: " + libro);
    }
}
