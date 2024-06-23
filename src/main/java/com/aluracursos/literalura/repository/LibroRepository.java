package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelo.CategoriaIdioma;
import com.aluracursos.literalura.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Interfaz que nos ayuda a acceder a la base de datos por medio de una consulta JPA
//Aqui pedimos el nombre del libro y el idioma
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findLibroBytitulo(String titulo);
    List<Libro> findLibrosByidioma(CategoriaIdioma idioma);


}
