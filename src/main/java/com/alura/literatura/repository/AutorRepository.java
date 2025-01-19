package com.alura.literatura.repository;

import com.alura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByNombre(String nombre);

    // Filtrar autores por años en los cuales estaban vivos
    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento <= :anio AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    // Encontrar autores por nombre
    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.anioNacimiento BETWEEN :inicio AND :fin OR a.anioFallecimiento BETWEEN :inicio AND :fin")
    List<Autor> findByRangoAnios(@Param("inicio") int inicio, @Param("fin") int fin);
}
