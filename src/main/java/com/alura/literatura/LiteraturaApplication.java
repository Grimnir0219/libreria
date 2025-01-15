package com.alura.literatura;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AutorRepository autorRepository, LibroRepository libroRepository) {
		return (args) -> {
			// Crear un autor
			Autor autor = new Autor("Gabriel García Márquez", 1927, 2014);
			autorRepository.save(autor);

			// Crear un libro asociado al autor
			Libro libro = new Libro("Cien años de soledad", "es", 50000, autor);
			libroRepository.save(libro);

			// Mostrar los datos insertados
			System.out.println("Autores:");
			autorRepository.findAll().forEach(System.out::println);

			System.out.println("Libros:");
			libroRepository.findAll().forEach(System.out::println);
		};
	}
}

