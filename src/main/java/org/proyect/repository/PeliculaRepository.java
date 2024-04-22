package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long>  {
    public List<Pelicula> findByNombre(String nombre);
}
