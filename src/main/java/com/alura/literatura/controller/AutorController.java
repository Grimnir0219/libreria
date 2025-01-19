package com.alura.literatura.controller;

import com.alura.literatura.model.Autor;
import com.alura.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    // Crear un nuevo autor
    @PostMapping
    public Autor crearAutor(@RequestBody Autor autor) {
        return autorRepository.save(autor);
    }

    @GetMapping
    public List<Autor> obtenerTodosLosAutores() {
        return autorRepository.findAll();
    }

    // Obtener todos los autores (endpoint para pruebas)
    @GetMapping("/todos-test")
    public List<Autor> testTodos() {
        return autorRepository.findAll();
    }

    // Buscar autores por nombre
    @GetMapping("/buscar")
    public List<Autor> buscarAutoresPorNombre(@RequestParam String nombre) {
        return autorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar autores vivos en un año específico
    @GetMapping("/vivos")
    public List<Autor> buscarAutoresVivosEnAnio(@RequestParam int anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    // Buscar autores por rango de años
    @GetMapping("/rango")
    public List<Autor> buscarAutoresPorRangoAnios(@RequestParam int inicio, @RequestParam int fin) {
        return autorRepository.findByRangoAnios(inicio, fin);
    }

    // Obtener un autor por ID
    @GetMapping("/id/{id}")
    public Autor obtenerAutorPorId(@PathVariable Long id) {
        return autorRepository.findById(id).orElse(null);
    }
}
