package Literalura.CHALLENGE.ALURA.Literalura.modelos;


import Literalura.CHALLENGE.ALURA.Literalura.DTO.LibroDTO;
import jakarta.persistence.*;



@Entity
@Table (name = "Libro")

public class Libro {

    @Id // id para identificar en base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // se genera automaticamente
    private Long id;

    @Column(unique = true) //columna unica
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER) //como solamente se toma el un solo autor por libro la relacion es muchos para uno, muchos libros para un autor
    @JoinColumn(name = "autor_id") // Llave foránea que referencia al autor
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Idiomas idioma;

    private Integer cantidadDescargas;

    public Libro(){}

    public Libro(Libro datosLibro){
        this.titulo = datosLibro.getTitulo();
        this.autor = datosLibro.getAutor();
        if (datosLibro.getIdioma()==null){

            throw new IllegalArgumentException("EL IDIOMA NO ES VÁLIDO");

        }else{

            this.idioma = Idiomas.valueOf(datosLibro.getIdioma());

        }

        this.cantidadDescargas = datosLibro.getCantidadDescargas();
    }


    public Libro(LibroDTO libroDTO){

        this.titulo=libroDTO.titulo();
        this.autor = new Autor(libroDTO.autor().get(0));
        //creo un Autor, para poder utilizar el autor del DTOAutor...
        this.cantidadDescargas=libroDTO.numeroDescargas();
        // Verificar y asignar el idioma si está presente.
        if (libroDTO.idioma() != null && !libroDTO.idioma().isEmpty()) {
            String idiomaStr = libroDTO.idioma().get(0); // Usar el primer idioma de la lista.
            try {
                this.idioma = Idiomas.fromString(idiomaStr); // Usar la funcion personalizada del enum.
            } catch (IllegalArgumentException e) {
                System.err.println("Idioma no válido: " + idiomaStr);
                this.idioma = null; // O asignar un idioma predeterminado si es necesario.
            }
        } else {
            this.idioma = null; // O asignar un idioma predeterminado si es necesario.
        }

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma.toString();
    }
    //no hay set de Idiomas ya que es un ENUM

    public Integer getCantidadDescargas() {
        return cantidadDescargas;
    }

    @Override
    public String toString() {
        return "\ntitulo=" + titulo +
                "\nAutor=" + autor.getNombreAutor() +
                "\nIdioma=" + idioma +
                "\nCantidad de Descargas=\n" + cantidadDescargas;
    }
}
