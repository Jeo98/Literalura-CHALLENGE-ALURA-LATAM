package Literalura.CHALLENGE.ALURA.Literalura.Principal;

import Literalura.CHALLENGE.ALURA.Literalura.DTO.AutorDTO;
import Literalura.CHALLENGE.ALURA.Literalura.DTO.Datos;
import Literalura.CHALLENGE.ALURA.Literalura.DTO.LibroDTO;
import Literalura.CHALLENGE.ALURA.Literalura.Repository.AutorRepository;
import Literalura.CHALLENGE.ALURA.Literalura.Repository.LibroRepository;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Autor;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Idiomas;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Libro;
import Literalura.CHALLENGE.ALURA.Literalura.service.BiblioService;
import Literalura.CHALLENGE.ALURA.Literalura.service.consumoAPI;
import Literalura.CHALLENGE.ALURA.Literalura.service.convierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MainPrincipal {

    public MainPrincipal(){

    }
    private int menu = 0, opciones;
    private final Scanner lectura = new Scanner(System.in);

    private final convierteDatos conversor = new convierteDatos();//construyo un conversor
    private final consumoAPI API = new consumoAPI();
    private final String API_URL = "https://gutendex.com/books/";

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private BiblioService biblioService;
    public void menu() {
        String JsonAPI = API.obtenerDatosAPI(API_URL); // obtengo los datos de la API
        var datos = conversor.obtenerDatos(JsonAPI, Datos.class);
        //convierto los datos de la API en tipo generico clase Datos, haciendo el mapeo a la clase LibroDTO

        //ahora trabajo directamente con la variable datos, que contiene los datos de la API
        while (menu == 0) {
            System.out.println("\n---- SCREENMATCH LIBROS MENU ----\n");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Buscar libro por autor");
            System.out.println("3 - Listar autores de libros consultados");
            System.out.println("4 - Listar autores vivos en determinado año");
            System.out.println("5 - Listar libros según idioma");
            System.out.println("6 - Listar libros consultados");
            System.out.println("7 - Estadísticas de descargas Literalura");
            System.out.println("--\n0 - CERRAR SCREENMATCH LIBROS\n");

            // Leer la opción seleccionada controlando el ingreso de datos
            try {
                System.out.print("Seleccione una opción: ");
                opciones = lectura.nextInt();
                while (opciones < 0 || opciones > 7) { // opcion dentro de las opciones validas
                    System.out.println("ERROR, ingrese una opción válida (0-7): ");
                    opciones = lectura.nextInt();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR, ingrese una opción válida (0-7): ");
                lectura.nextLine(); // Limpiar buffer
                continue;
            }

            switch (opciones) {
                case 1:
                    buscarLibroPorTitulo();
                    break;

                case 2:
                    buscarLibroAutor();
                    break;

                case 3:
                    listarAutoresLibrosConsultados();
                    break;

                case 4:
                   listarAutoresVivosPorAnio();
                    break;

                case 5:
                    listarLibrosPorIdioma();
                    break;

                case 6:
                    listarLibrosConsultados();
                    break;

                case 7:
                    mostrarEstadisticas();
                    break;

                case 0:
                    System.out.println("\n-> CIERRE | GRACIAS POR UTILIZAR LA BIBLIOTECA <-\n");
                    menu = 1; // Cerrar el menú
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void listarLibrosPorIdioma() {
        lectura.nextLine();
        System.out.println("Ingresar idioma que quiere buscar: ");
         String idiomaBuscado = lectura.nextLine();
        try {
        // Convertir a enum Idiomas
            Idiomas idiomaEnum = Idiomas.fromTotalString(idiomaBuscado);

            // Filtrar libros por idioma
            List<LibroDTO> libros = biblioService.ListarLibrosPorIdioma(idiomaEnum);
            if(libros.isEmpty()){
                System.out.println("No hay libros registrados en la base de datos.");
                return;
            }
            for (LibroDTO libro : libros) {
                System.out.println("\nTítulo: " + libro.titulo());
                System.out.println("Autor: " + libro.autor().get(0).nombreAutor().replace(",",""));
                System.out.println("Idioma: " + libro.idioma());
                System.out.println("--------------------------------------------");
            }

        } catch (InputMismatchException e){
            System.out.println("ERROR: Ocurrió un error al listar los libros...");
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("\n-> ESTADÍSTICAS DE LA BIBLIOTECA <-\n");
        try {
            // Obtener los libros desde la base de datos
            List<LibroDTO> librosEnBD = biblioService.ListarLibrosEnBD();

            if (librosEnBD.isEmpty()) {
                System.out.println("No hay libros en la base de datos para realizar estadísticas.");
                return;
            }

            // Calcular estadísticas sobre el campo "cantidadDescargas"
            IntSummaryStatistics estadisticas = librosEnBD.stream()
                    .filter(libro -> libro.numeroDescargas() != null && libro.numeroDescargas() > 0)
                    .collect(Collectors.summarizingInt(LibroDTO::numeroDescargas));

            // Mostrar estadísticas
            System.out.println("Cantidad media de descargas en biblioteca: " + estadisticas.getAverage() + " descargas");
            System.out.println("Cantidad máxima de descargas en biblioteca: " + estadisticas.getMax()+ " descargas");
            System.out.println("Cantidad mínima de descargas en biblioteca: " + estadisticas.getMin()+ " descargas");
            System.out.println("Cantidad de libros consultados en biblioteca para realizar estadísticas: " + estadisticas.getCount()+ " libros en base de datos");
        } catch (Exception e) {
            System.out.println("Ocurrió un error al generar las estadísticas: " + e.getMessage());
        }

    }

    private void listarLibrosConsultados() {
        lectura.nextLine();
        try {
            List<LibroDTO> libros = biblioService.ListarLibrosEnBD();

            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en la base de datos.");
                return;
            }

            System.out.println("\n-> LIBROS CONSULTADOS EN LA BIBLIOTECA <-\n");

            for (LibroDTO libro : libros) {
                System.out.println("\nTítulo: " + libro.titulo());
                System.out.println("Autor: " + libro.autor().get(0).nombreAutor().replace(",",""));
                System.out.println("Idioma: " + libro.idioma());
                System.out.println("Descargas: " + libro.numeroDescargas());
                System.out.println("--------------------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al listar los libros consultados: " + e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------
    private void listarAutoresVivosPorAnio() {
        lectura.nextLine();
        System.out.println("\n-> LISTAR AUTORES VIVOS POR AÑO <-\n");

        try {
            System.out.print("Ingrese el año a buscar: ");
            String entrada = lectura.nextLine().trim();

            // Validar si la entrada es un número entero válido
            if (!entrada.matches("\\d+")) {
                System.out.println("ERROR: El dato ingresado no es un número válido.");
                return;
            }

            int anio = Integer.parseInt(entrada);

            if (anio < 0) {
                System.out.println("ERROR: El año debe ser mayor o igual a 0.");
                return;
            }

            // Consultar autores vivos en el año
            List<AutorDTO> autores = biblioService.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio + ".");
            } else {
                System.out.println("\nAutores vivos en el año " + anio + ":\n");
                autores.forEach(autor -> System.out.printf(
                        "Nombre: %s | Año de Nacimiento: %s | Año de Fallecimiento: %s%n",
                        autor.nombreAutor(),
                        autor.anioNacimiento() != null ? autor.anioNacimiento() : "Desconocido",
                        autor.anioFallecimiento() != null ? autor.anioFallecimiento() : "Vivo"
                ));
            }
        } catch (Exception e) {
            System.out.println("ERROR: Ocurrió un problema al procesar la solicitud: " + e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------
    private void listarAutoresLibrosConsultados() {
        lectura.nextLine();
        List<AutorDTO> autores = biblioService.listarAutoresRegistrados();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n-> AUTORES REGISTRADOS <-\n");
            autores.forEach(autor -> System.out.printf(
                    "Nombre: %s | Año de Nacimiento: %s | Año de Fallecimiento: %s%n",
                    autor.nombreAutor(),
                    autor.anioNacimiento() != null ? autor.anioNacimiento() : "Desconocido",
                    autor.anioFallecimiento() != null ? autor.anioFallecimiento() : "Vivo"
            ));
        }

    }
//-------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------
    private void buscarLibroAutor() {
        lectura.nextLine();
        System.out.println("\n-> BUSQUEDA DE LIBRO POR AUTOR <-\n");

        System.out.print("Ingrese el nombre del autor: ");
        String nombre = lectura.nextLine().trim();

        if (nombre.isEmpty()) {
            System.out.println("El nombre del autor no puede estar vacío.");
            return;
        }

        try {
            // Buscar el libro relacionado con el autor
            Optional<LibroDTO> libroAutorBuscado = biblioService.buscarLibrosPorAutor(nombre);

            if (libroAutorBuscado.isPresent()) {
                System.out.println("Libro encontrado!");
                System.out.println(libroAutorBuscado.get());
            } else {
                System.out.println("Autor no encontrado en la base de datos. Buscando en la API...");

                // Buscar al autor en la API
                String url = API_URL + "?search=" + nombre.replace(" ", "+");
                String json = API.obtenerDatosAPI(url);
                Datos datos = conversor.obtenerDatos(json, Datos.class);

                // Filtrar libros que coincidan con el autor
                List<LibroDTO> librosDelAutor = datos.resultadoslibros().stream()
                        .filter(libro -> libro.autor().stream()
                                .anyMatch(autor -> autor.nombreAutor().equalsIgnoreCase(nombre)))
                        .toList();

                if (librosDelAutor.isEmpty()) {
                    System.out.println("No se encontraron libros para el autor en la API.");
                    return;
                }

                // Guardar autor y sus libros en la base de datos
                Autor nuevoAutor = new Autor(librosDelAutor.get(0).autor().get(0));
                nuevoAutor = autorRepository.save(nuevoAutor);
                System.out.println("Autor guardado en la base de datos: " + nuevoAutor.getNombreAutor());

                for (LibroDTO libroDTO : librosDelAutor) {
                    Libro nuevoLibro = new Libro(libroDTO);
                    nuevoLibro.setAutor(nuevoAutor);
                    libroRepository.save(nuevoLibro);
                    System.out.println("Libro guardado en la base de datos: " + nuevoLibro.getTitulo());
                }
            }
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e.getMessage());
        }
    }
//-------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------

    public void buscarLibroPorTitulo() {
        lectura.nextLine(); // Limpia el buffer

        System.out.println("\n-> BUSQUEDA DE LIBRO POR TITULO <-\n");
        System.out.print("Ingrese titulo del libro: ");
        String titulo = lectura.nextLine();

        while (titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío. Ingrese nuevamente: ");
            titulo=lectura.next();
        }
        try {
            // Buscar en la base de datos
            Optional<Libro> libroEncontrado = buscarEnBaseDeDatos(titulo);

            if (libroEncontrado.isPresent()) {
                System.out.println("Libro encontrado en base de datos:");
                System.out.println(libroEncontrado.get().toString());
            } else {
                // Buscar en la API y guardar en la base de datos
                System.out.println("LIBRO NO CARGADO EN  BASE DE DATOS... BUSCANDO EN API...");
                LibroDTO libroDesdeAPI = buscarEnAPI(titulo);
                if (libroDesdeAPI == null) {
                    System.out.println("Libro no encontrado en la API.");
                    return;
                }

                Libro libroGuardado = guardarLibro(libroDesdeAPI);
                System.out.println("Libro cargado en BD con éxito:");
                System.out.println(libroGuardado);
            }

        } catch (Exception e) {
            System.out.println("Ha ocurrido un problema: " + e.getMessage());
        }
    }

        private Optional<Libro> buscarEnBaseDeDatos(String titulo) {
            return libroRepository.findByTituloContainingIgnoreCase(titulo);
        }

        private LibroDTO buscarEnAPI(String titulo) {
            String url = API_URL + "?search=" + titulo.replace(" ", "+");
            String json = API.obtenerDatosAPI(url);

            try {
                Datos datos = conversor.obtenerDatos(json, Datos.class);
                return datos.resultadoslibros().stream()
                        .filter(libro -> libro.titulo().equalsIgnoreCase(titulo))
                        .findFirst()
                        .orElse(null);
            } catch (Exception e) {
                System.out.println("Error al procesar los datos de la API: " + e.getMessage());
                return null;
            }
        }

        private Libro guardarLibro(LibroDTO datosBusqueda) {
            Autor autor = autorRepository.findBynombreAutorIgnoreCase(datosBusqueda.autor().get(0).nombreAutor().replace(",",""))
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor(datosBusqueda.autor().get(0));
                        if (nuevoAutor.getYearNacimiento() == null) {
                            nuevoAutor.setYearNacimiento(0); // Asignar valor por defecto
                        }
                        if (nuevoAutor.getYearFallecimiento() == null) {
                            nuevoAutor.setYearFallecimiento(0); // Asignar valor por defecto
                        }
                        return autorRepository.save(nuevoAutor);
                    });

            Libro libro = new Libro(datosBusqueda);
            libro.setAutor(autor);

            return libroRepository.save(libro);
        }
//-------------------------------------------------------------------------------------------------------
    }//fin clase Principal