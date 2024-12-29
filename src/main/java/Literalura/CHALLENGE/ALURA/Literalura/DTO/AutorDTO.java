package Literalura.CHALLENGE.ALURA.Literalura.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorDTO(

        @JsonAlias("name") String nombreAutor,
        @JsonAlias("birth_year") Integer anioNacimiento,
        @JsonAlias("death_year") Integer anioFallecimiento) {
}
