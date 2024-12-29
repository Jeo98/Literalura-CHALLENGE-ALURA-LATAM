package Literalura.CHALLENGE.ALURA.Literalura.service;

import Literalura.CHALLENGE.ALURA.Literalura.DTO.AutorDTO;
import Literalura.CHALLENGE.ALURA.Literalura.DTO.LibroDTO;

import Literalura.CHALLENGE.ALURA.Literalura.Repository.AutorRepository;
import Literalura.CHALLENGE.ALURA.Literalura.Repository.LibroRepository;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Autor;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Idiomas;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BiblioService {
    @Autowired
    private final LibroRepository libroRepository;
    @Autowired
    private final AutorRepository autorRepository;

    @Autowired
    public BiblioService(LibroRepository libroRepository, AutorRepository autorRepository) {

        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public List<AutorDTO> convertirAAutorDTO(List<Autor> autores) {
        return autores.stream()
                .map(autor -> new AutorDTO(
                        autor.getNombreAutor(),
                        autor.getYearNacimiento(),
                        autor.getYearFallecimiento()
                ))
                .collect(Collectors.toList());
    }
    public List<LibroDTO> convertirALibroDTO(List<Libro> libros) {
        return libros.stream()
                .map(l -> new LibroDTO(
                        l.getTitulo(),
                        List.of(new AutorDTO(
                                l.getAutor().getNombreAutor(),
                                l.getAutor().getYearNacimiento(),
                                l.getAutor().getYearFallecimiento()
                        )),
                        List.of(l.getIdioma()), // Convertimos el idioma único en una lista
                        l.getCantidadDescargas()
                ))
                .collect(Collectors.toList());
    }

    public Optional<LibroDTO> buscarLibrosPorAutor(String nombreAutor) {
        // Consulta el primer libro cuyo autor coincida
        Optional<Libro> libroEncontrado = libroRepository.findFirstByAutorNombreIgnoreCase(nombreAutor);

        return libroEncontrado.map(libro -> new LibroDTO(
                libro.getTitulo(),
                List.of(new AutorDTO(
                        libro.getAutor().getNombreAutor(),
                        libro.getAutor().getYearNacimiento(),
                        libro.getAutor().getYearFallecimiento()
                )), // Convierte el autor en una lista de AutorDTO
                libro.getIdioma() != null ? List.of(libro.getIdioma()) : List.of("Idioma desconocido"), // Convierte el idioma en una lista
                libro.getCantidadDescargas()
        ));
    }

    public List<LibroDTO> ListarLibrosEnBD() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) { // si no tiene nada cargado...
            System.out.println("No hay libros registrados en la base de datos.");
        }
        return convertirALibroDTO(libroRepository.findAll());

    }

    public List<LibroDTO> ListarLibrosPorIdioma(Idiomas idioma) {

        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros para el idioma: " + idioma);
        }
        return convertirALibroDTO(libros);

    }

    public void obtenerLibroPorTitulo(String titulo) {

        Optional<Libro> libroBuscado = libroRepository.findByTituloContainingIgnoreCase(titulo);
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado! ");
            System.out.println(libroBuscado.get());

        }
        else {
            System.out.println("Libro no encontrado...");
        }

    }

    public List<AutorDTO> listarAutoresRegistrados() {

        return convertirAAutorDTO(autorRepository.findAll());
    }

    public List<AutorDTO> listarAutoresVivosEnAnio(int anio) {
        return convertirAAutorDTO(autorRepository.findAutoresVivosEnAnio(anio));
    }

}
