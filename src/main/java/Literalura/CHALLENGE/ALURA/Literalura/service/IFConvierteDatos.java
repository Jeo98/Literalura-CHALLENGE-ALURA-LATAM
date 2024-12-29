package Literalura.CHALLENGE.ALURA.Literalura.service;

public interface IFConvierteDatos {

    <T> T obtenerDatos(String json, Class <T> clase);
    //<T> T se utiliza para especificar que el retorno es genérico, es decir, retorna cualquier tipo de dato
    //Class <T> clase, es un parámetro clase de tipo genérico

}
