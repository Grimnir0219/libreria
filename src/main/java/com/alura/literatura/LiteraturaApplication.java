package com.alura.literatura;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AutorRepository autorRepository, LibroRepository libroRepository, GutendexService gutendexService) {
		return (args) -> {

			// Crear un autor
//			Autor autor = new Autor("Gabriel García Márquez", 1927, 2014);
//			autorRepository.save(autor);
//
//			// Crear un libro asociado al autor
//			Libro libro = new Libro("Cien años de soledad", "es", 50000, autor);
//			libroRepository.save(libro);

			// Mostrar los datos insertados
			System.out.println("Autores:");
			autorRepository.findAll().forEach(System.out::println);

			System.out.println("Libros:");
			libroRepository.findAll().forEach(System.out::println);

			// Menú interactivo
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
					System.out.println("6. Salir de la aplicación");
					System.out.print("Seleccione una opción: ");

					if (!scanner.hasNextInt()) {
						System.out.println("Entrada no válida. Por favor, ingrese un número.");
						scanner.next(); // Limpiar entrada inválida
						continue;
					}

					int opcion = scanner.nextInt();
					scanner.nextLine(); // Limpiar el buffer

					switch (opcion) {
						case 1:
							buscarLibroPorTitulo(scanner, libroRepository, gutendexService);
							break;
						case 2:
							listarLibrosRegistrados(libroRepository);
							break;
						case 3:
							listarAutoresRegistrados(autorRepository);
							break;
						case 4:
							listarAutoresVivos(scanner, autorRepository);
							break;
						case 5:
							listarLibrosPorIdioma(scanner, libroRepository);
							break;
						case 6:
							System.out.println("Saliendo de la aplicación...");
							continuar = false; // Salir del bucle
							break;
						default:
							System.out.println("Opción no válida. Intente nuevamente.");
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					scanner.nextLine(); // Limpiar el buffer en caso de excepción
				}
			}

			scanner.close(); // Cerrar el escáner al finalizar el bucle
		};
	}

	private void buscarLibroPorTitulo(Scanner scanner, LibroRepository libroRepository, GutendexService gutendexService) {
		System.out.print("Ingrese el título del libro: ");
		String titulo = scanner.nextLine();
		try {
			Libro libro = gutendexService.buscarLibroPorTitulo(titulo);
			libroRepository.save(libro);
			System.out.println("Libro encontrado y registrado: " + libro);
		} catch (RuntimeException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void listarLibrosRegistrados(LibroRepository libroRepository) {
		List<Libro> libros = libroRepository.findAll();
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados.");
		} else {
			libros.forEach(System.out::println);
		}
	}

	private void listarAutoresRegistrados(AutorRepository autorRepository) {
		List<Autor> autores = autorRepository.findAll();
		if (autores.isEmpty()) {
			System.out.println("No hay autores registrados.");
		} else {
			autores.forEach(System.out::println);
		}
	}

	private void listarAutoresVivos(Scanner scanner, AutorRepository autorRepository) {
		System.out.print("Ingrese el año para buscar autores vivos: ");
		if (!scanner.hasNextInt()) {
			System.out.println("Entrada no válida. Por favor, ingrese un número válido.");
			scanner.next(); // Limpiar entrada inválida
			return;
		}
		int anio = scanner.nextInt();
		scanner.nextLine(); // Limpiar el buffer
		List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
		if (autoresVivos.isEmpty()) {
			System.out.println("No se encontraron autores vivos en el año " + anio);
		} else {
			autoresVivos.forEach(System.out::println);
		}
	}

	private void listarLibrosPorIdioma(Scanner scanner, LibroRepository libroRepository) {
		System.out.print("Ingrese el idioma para filtrar libros: ");
		String idioma = scanner.nextLine();
		List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
		if (librosPorIdioma.isEmpty()) {
			System.out.println("No se encontraron libros en el idioma " + idioma);
		} else {
			librosPorIdioma.forEach(System.out::println);
		}
	}
}
