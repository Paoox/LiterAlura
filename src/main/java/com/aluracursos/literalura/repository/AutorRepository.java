package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

//Es la interfaz donde se define la query para la base de datos aqui nos apoyamos de JpaRepository
//para traer de la tabla autor el año de fallecimiento
public interface AutorRepository extends JpaRepository<Autor,Long>{
    Optional<Autor> findBynombre(String nombre);
    @Query("SELECT a FROM Autor a WHERE  a.yearFallecimiento > :Year")
    List<Autor> findAutoresByYear(Integer Year);
}
