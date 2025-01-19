package com.alura.literatura.repository;

import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    //Muestra los idiomas disponibles según los libros registrados en la base de datos
    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> findIdiomasDisponibles();

    //Filtra libros por Idioma
    List<Libro> findByIdioma(String idioma);

    // Método para obtener los 10 libros más descargados
    List<Libro> findTop10ByOrderByDescargasDesc();

    // Obtener Libro por titulo
    List<Libro> findByTitulo(String titulo);
}
