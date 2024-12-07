package com.alura.literatura_challenge.repository;

import com.alura.literatura_challenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    Autor findByNombreIgnoreCase(String nombre);
    List<Autor> findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(int anioInicial, int anioFinal);
}
