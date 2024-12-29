package Literalura.CHALLENGE.ALURA.Literalura.DTO;


import Literalura.CHALLENGE.ALURA.Literalura.modelos.Autor;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//DTO que voy a utilizar para mapear y usar solo la info que pido en los parametros
@JsonIgnoreProperties(ignoreUnknown = true)
public record LibroDTO(

    @JsonAlias("title") String titulo,
    @JsonAlias("authors") List<AutorDTO> autor,
    @JsonAlias("languages") List<String> idioma,
    @JsonAlias("download_count") Integer numeroDescargas){
}
