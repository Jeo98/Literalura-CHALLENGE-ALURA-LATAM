package Literalura.CHALLENGE.ALURA.Literalura.Repository;


import Literalura.CHALLENGE.ALURA.Literalura.modelos.Idiomas;
import Literalura.CHALLENGE.ALURA.Literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTituloContainingIgnoreCase(String nombreLibro);
    List<Libro> findByIdioma(Idiomas Idioma);

    @Query("SELECT l FROM Libro l WHERE LOWER(l.autor.nombreAutor) = LOWER(:nombreAutor)")
    Optional<Libro> findFirstByAutorNombreIgnoreCase(@Param("nombreAutor") String nombreAutor);

    @Query("SELECT l FROM Libro l WHERE l.autor.nombreAutor = :nombreAutor")
    List<Libro> findByAutorNombre (@Param("nombreAutor") String nombreAutor);

    List<Libro> findAll();

    @Query("SELECT COUNT(l) FROM Libro l WHERE l.idioma = :idioma")
    Long contarLibrosPorLenguaje(@Param("idioma") Idiomas idioma); // cambiar lenguaje por lenguajes

    @Query("SELECT l FROM Libro l WHERE l.autor.Id = :idAutor")
    List<Libro> findLibrosByAutorId(@Param("idAutor") Long idAutor);

}
