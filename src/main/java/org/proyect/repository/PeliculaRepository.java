package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    public List<Pelicula> findByTitulo(String titulo);
    public List<Pelicula> findByIdElemento(Long idElemento);

    // default List<Pelicula> findLastFourMovies() {
    //     Pageable pageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "fecha_salida"));
    //     Page<Pelicula> page = findAll(pageable);
    //     return page.getContent();
    // }


    public Pelicula getByTitulo(String titulo);


}
