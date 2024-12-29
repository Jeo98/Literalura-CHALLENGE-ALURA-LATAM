package Literalura.CHALLENGE.ALURA.Literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class convierteDatos implements  IFConvierteDatos{
    private ObjectMapper objectMapper = new ObjectMapper();
    // permite mapear los valores que vienen de la API
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
            //objectMapper va a leer el valor que viene en el json y lo convierte al tipo generico
            //debe estar entre un try-catch por si ocurre error(intellij notifica del error si no está)
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
