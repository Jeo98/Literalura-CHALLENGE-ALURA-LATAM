package Literalura.CHALLENGE.ALURA.Literalura.Repository;

import Literalura.CHALLENGE.ALURA.Literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.yearNacimiento <= :anio AND (a.yearFallecimiento IS NULL OR a.yearFallecimiento > :anio)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    Optional<Autor> findBynombreAutorIgnoreCase(String s);

}
