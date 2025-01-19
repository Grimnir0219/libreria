package com.alura.literatura.service;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GutendexServiceTest {

    private GutendexService gutendexService;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gutendexService = new GutendexService();
        gutendexService.libroRepository = libroRepository;
        gutendexService.autorRepository = autorRepository;
    }

    @Test
    void buscarYRegistrarLibroEnAPI_libroNuevo_registraCorrectamente() throws Exception {
        // Configuración de datos de prueba
        String titulo = "Don Quijote";
        Autor autor = new Autor("Miguel de Cervantes", 1547, 1616);
        Libro libro = new Libro(titulo, "es", 1000, autor);

        when(autorRepository.findByNombre("Miguel de Cervantes")).thenReturn(List.of());
        when(libroRepository.findByTitulo(titulo)).thenReturn(List.of());

        // Acción
        gutendexService.buscarYRegistrarLibroEnAPI(titulo);

        // Verificaciones
        verify(autorRepository, times(1)).save(autor);
        verify(libroRepository, times(1)).save(libro);
    }

    @Test
    void buscarYRegistrarLibroEnAPI_libroExistente_noDuplicaRegistro() throws Exception {
        // Configuración de datos de prueba
        String titulo = "Don Quijote";
        Autor autor = new Autor("Miguel de Cervantes", 1547, 1616);
        Libro libro = new Libro(titulo, "es", 1000, autor);

        when(libroRepository.findByTitulo(titulo)).thenReturn(List.of(libro));

        // Acción
        gutendexService.buscarYRegistrarLibroEnAPI(titulo);

        // Verificaciones
        verify(libroRepository, never()).save(any());
    }
}
