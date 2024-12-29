package Literalura.CHALLENGE.ALURA.Literalura.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(@JsonAlias("results") List<LibroDTO> resultadoslibros) {
    //La api arroja como resultado una lista con todos los datos de los libros, por ello se
    //realiza este record que luego se utilizará para extraer la informacion
}
