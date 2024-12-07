package com.alura.literatura_challenge.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;
    @Column(name = "nombre_autor")
    private String nombreAutor;
    private String idioma;
    private Integer descargas;

    public Libros(){}

    public Libros(DatosLibro datosLibro, Autor autor){
        this.titulo = datosLibro.titulo();
        this.autor = autor;
        this.nombreAutor = autor.getNombre();
        setIdioma(datosLibro.idiomas());
        this.descargas = datosLibro.descargas();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public List<String>  getIdioma() {
        return Arrays.asList(idioma.split(","));
    }

    public void setIdioma(List<String> idioma) {
        this.idioma =  String.join(",", idioma);
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return  "\n--------------------------------------------\n" +
                "Libro: \n" +
                "Título = " + titulo + "\n" +
                "Autor = " + autor + "\n" +
                "Idioma = " + idioma + "\n" +
                "Número de descargas = " + descargas +
                "\n--------------------------------------------\n";
    }
}