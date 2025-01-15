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

    // Obtener todos los autores
    @GetMapping
    public List<Autor> obtenerAutores() {
        return autorRepository.findAll();
    }

    // Crear un nuevo autor
    @PostMapping
    public Autor crearAutor(@RequestBody Autor autor) {
        return autorRepository.save(autor);
    }

    // Obtener un autor por ID
    @GetMapping("/{id}")
    public Autor obtenerAutorPorId(@PathVariable Long id) {
        return autorRepository.findById(id).orElse(null);
    }

    // Eliminar un autor por ID
    @DeleteMapping("/{id}")
    public void eliminarAutor(@PathVariable Long id) {
        autorRepository.deleteById(id);
    }

    //Obtener autor vivo por año específico
    @GetMapping("/vivos")
    public List<Autor> obtenerAutoresVivosEnAnio(@RequestParam int anio) {
        List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);
        if (autores.isEmpty()){
            throw new RuntimeException("No se encontraron autores vivos de este año " + anio);
        }
        return autores;
    }


}

