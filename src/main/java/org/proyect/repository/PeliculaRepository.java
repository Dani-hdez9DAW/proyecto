package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.domain.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    public List<Pelicula> findByTitulo(String titulo);

    public List<Pelicula> findByIdElemento(Long idElemento);

    public Pelicula getByTitulo(String titulo);

    public List<Pelicula> findByCategorias(Categoria categoria);

    public List<Pelicula> findByClasificacion(String clasificacion);
}