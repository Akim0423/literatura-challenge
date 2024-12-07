package com.alura.literatura_challenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AutorDatos> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer descargas) {
}