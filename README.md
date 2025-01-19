Gestión de Libros con Gutendex API

Descripción
Esta aplicación permite gestionar información sobre libros utilizando la API de Gutendex. Los usuarios pueden buscar libros, registrar información y realizar diversas consultas como listar libros por idioma o autores vivos en un determinado año.

Funcionalidades principales

Buscar libro por título:

Consulta la API de Gutendex para encontrar libros que coincidan con el título especificado.
Permite seleccionar un libro para registrarlo en la base de datos.
Listar libros registrados:

Muestra todos los libros almacenados en la base de datos junto con su título, autor, idioma y descargas.
Listar autores registrados:

Muestra los autores registrados con su nombre, año de nacimiento y año de fallecimiento (si aplica).
Listar autores vivos en un determinado año:

Filtra y muestra los autores vivos en el año especificado por el usuario.
Listar libros por idioma:

Muestra los idiomas disponibles en la base de datos y permite filtrar libros según el idioma seleccionado.
Top 10 libros más descargados:

Lista los 10 libros registrados con más descargas.
Requisitos

Java 17 o superior
Spring Boot 3.0
PostgreSQL para la base de datos
Maven para gestión de dependencias

Configuración

1. Clona el repositorio:
git clone https://github.com/tu-usuario/tu-repositorio.git

2. Configura la conexión a la base de datos en el archivo application.properties:properties
spring.datasource.url=jdbc:postgresql://localhost:5432/liter_alura
spring.datasource.username=tu-usuario
spring.datasource.password=tu-contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3. Construye el proyecto con Maven:
mvn clean install

4. Ejecuta la aplicación:
java -jar target/tu-archivo.jar

Uso
Menú interactivo
Cuando ejecutes la aplicación, se mostrará un menú como este:

===== Menú de Opciones =====
1. Buscar libro por título
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un determinado año
5. Listar libros por idioma
6. Top 10 libros más descargados
7. Salir de la aplicación

   
Estructura del proyecto

src/
├── main/
│   ├── java/
│   │   ├── com.alura.literatura/
│   │   │   ├── menu/  -> Menú interactivo de la aplicación
│   │   │   ├── model/ -> Modelos (Autor, Libro)
│   │   │   ├── repository/ -> Repositorios JPA
│   │   │   ├── service/ -> Lógica de negocio y conexión con Gutendex API
│   │   ├── resources/
│   │       ├── application.properties -> Configuración de la aplicación
├── test/ -> Pruebas unitarias y de integración


Pruebas
JUnit 5: Se recomienda escribir pruebas unitarias para los métodos críticos.
Ejecuta las pruebas con:
mvn test

API de Gutendex
La aplicación consume la API de Gutendex:
URL base: https://gutendex.com/books/
Parámetros soportados:
search: Palabra clave para buscar libros.
languages: Filtrar por idioma.
author_year_start y author_year_end: Filtrar por años de los autores.





