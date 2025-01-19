package com.alura.literatura.menu;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class MenuInteractivo implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GutendexService gutendexService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n===== Menú de Opciones =====");
                System.out.println("1. Buscar libro por título");
                System.out.println("2. Listar libros registrados");
                System.out.println("3. Listar autores registrados");
                System.out.println("4. Listar autores vivos en un determinado año");
                System.out.println("5. Listar libros por idioma");
                System.out.println("6. Top 10 libros más descargados");
                System.out.println("7. Salir de la aplicación");
                System.out.print("Seleccione una opción: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Entrada no válida. Por favor, ingrese un número.");
                    scanner.next(); // Limpiar entrada no válida
                    continue;
                }

                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        buscarYRegistrarLibroEnAPI(scanner);
                        break;
                    case 2:
                        listarLibros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivos(scanner);
                        break;
                    case 5:
                        listarLibrosPorIdioma(scanner);
                        break;
                    case 6:
                        mostrarTop10Libros();
                        break;
                    case 7:
                        System.out.println("Saliendo de la aplicación...");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer en caso de error
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                scanner.nextLine(); // Limpiar el buffer en caso de error
            }
        }

        scanner.close();
    }

    private void buscarYRegistrarLibroEnAPI(Scanner scanner) {
        System.out.print("Ingrese una palabra para buscar en los títulos de los libros: ");
        String palabra = scanner.nextLine();

        try {
            List<Libro> librosEncontrados = gutendexService.buscarLibrosPorPalabra(palabra, 10);

            if (librosEncontrados.isEmpty()) {
                System.out.println("No se encontraron libros con la palabra \"" + palabra + "\".");
                return;
            }

            System.out.println("Libros encontrados:");
            for (int i = 0; i < librosEncontrados.size(); i++) {
                System.out.println((i + 1) + ". " + librosEncontrados.get(i));
            }

            System.out.print("Seleccione un libro para registrarlo (1-" + librosEncontrados.size() + "): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Entrada no válida. Cancelando registro.");
                scanner.next();
                return;
            }

            int seleccion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            if (seleccion < 1 || seleccion > librosEncontrados.size()) {
                System.out.println("Selección fuera de rango. Cancelando registro.");
                return;
            }

            Libro libroSeleccionado = librosEncontrados.get(seleccion - 1);

            List<Libro> existentes = libroRepository.findByTitulo(libroSeleccionado.getTitulo());
            if (!existentes.isEmpty()) {
                System.out.println("El libro \"" + libroSeleccionado.getTitulo() + "\" ya está registrado en la base de datos.");
                return;
            }

            libroRepository.save(libroSeleccionado);
            System.out.println("Libro registrado exitosamente: " + libroSeleccionado);
        } catch (Exception e) {
            System.out.println("Ocurrió un error al buscar libros en la API de Gutendex: " + e.getMessage());
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            libros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Autores registrados:");
            autores.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombre() +
                        (autor.getAnioNacimiento() != null ? ", Año de nacimiento: " + autor.getAnioNacimiento() : "") +
                        (autor.getAnioFallecimiento() != null ? ", Año de fallecimiento: " + autor.getAnioFallecimiento() : ""));
            });
        }
    }

    //Obtener autores por fe año en que están o estuvieron vivos
    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada no válida. Por favor, ingrese un número.");
            scanner.next(); // Limpiar entrada no válida
            return;
        }

        int anio = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        List<Autor> autores = autorRepository.findAll();

        List<Autor> autoresVivos = autores.stream()
                .filter(autor -> autor.getAnioNacimiento() != null && autor.getAnioNacimiento() <= anio)
                .filter(autor -> autor.getAnioFallecimiento() == null || autor.getAnioFallecimiento() >= anio)
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(autor -> {
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Fecha de nacimiento: " +
                        (autor.getAnioNacimiento() != null ? autor.getAnioNacimiento() : "Desconocido"));
                System.out.println("Fecha de fallecimiento: " +
                        (autor.getAnioFallecimiento() != null ? autor.getAnioFallecimiento() : "Aún con vida"));
                System.out.println("--------------------------");
            });
        }
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        // Obtener idiomas únicos registrados en la base de datos
        List<String> idiomasDisponibles = libroRepository.findIdiomasDisponibles();

        if (idiomasDisponibles.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        // Mostrar los idiomas disponibles
        System.out.println("Idiomas disponibles:");
        idiomasDisponibles.forEach(idioma -> System.out.println("- " + idioma));

        System.out.print("Seleccione un idioma de la lista: ");
        String idiomaSeleccionado = scanner.nextLine();

        // Obtener libros del repositorio filtrados por idioma
        List<Libro> libros = libroRepository.findByIdioma(idiomaSeleccionado);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + idiomaSeleccionado + "'.");
        } else {
            System.out.println("Libros en el idioma '" + idiomaSeleccionado + "':");
            libros.forEach(libro -> {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
                System.out.println("Descargas: " + libro.getDescargas());
                System.out.println("--------------------------");
            });
        }
    }

    // Mostrar 10 libros más descargados de la base de datos.
    private void mostrarTop10Libros() {
        System.out.println("Top 10 libros más descargados:");
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByDescargasDesc();

        if (top10Libros.isEmpty()) {
            System.out.println("No hay libros registrados para mostrar el Top 10.");
        } else {
            top10Libros.forEach(libro -> {
                System.out.println("Titulo: " + libro.getTitulo() + ", Descargas: " + libro.getDescargas());
            });
        }
    }
}
