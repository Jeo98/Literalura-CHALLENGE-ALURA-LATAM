package Literalura.CHALLENGE.ALURA.Literalura.modelos;


import Literalura.CHALLENGE.ALURA.Literalura.DTO.AutorDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreAutor;

    @Column(name = "yearNacimiento",nullable = false)
    private int yearNacimiento=0;

    @Column(name = "yearFallecimiento",nullable = false)
    private int yearFallecimiento=0;

    @OneToMany(mappedBy = "autor")
    private List<Libro> libro = new ArrayList<>();

    public Autor(){
        //constructor
    }

    public Autor(AutorDTO datosAutor) {
        this.nombreAutor = datosAutor.nombreAutor();
        this.yearNacimiento = datosAutor.anioNacimiento() != null ? datosAutor.anioNacimiento() : 0; // Default a 0  inicializo atributo
        this.yearFallecimiento = datosAutor.anioFallecimiento() != null ? datosAutor.anioFallecimiento() : 0; // Default a 0 inicializo atributo
        //constructor por parametros
    }

    public List<Libro> getLibro() {
        return libro;
    }
    public Integer getYearFallecimiento() {
        return yearFallecimiento;
    }

    public void setYearFallecimiento(Integer yearFallecimiento) {
        this.yearFallecimiento = yearFallecimiento;
    }

    public Integer getYearNacimiento() {
        return yearNacimiento;
    }

    public void setYearNacimiento(Integer yearNacimiento) {
        this.yearNacimiento = yearNacimiento;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "nombre Autor='" + nombreAutor + '\'' +
                "Nacimiento=" + yearNacimiento +
                "Fallecimiento=" + yearFallecimiento ;
    }
}
