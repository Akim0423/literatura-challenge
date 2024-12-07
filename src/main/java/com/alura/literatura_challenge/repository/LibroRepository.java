package com.alura.literatura_challenge.repository;

import com.alura.literatura_challenge.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros,Long> {
    Libros findByTitulo(String titulo);
    List<Libros> findByIdiomaContaining(String idioma);
}
