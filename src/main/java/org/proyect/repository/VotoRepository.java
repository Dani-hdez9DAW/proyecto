package org.proyect.repository;

import org.proyect.domain.Voto;
import org.proyect.domain.Usuario;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    Voto findByUsuarioAndPelicula(Usuario usuario, Pelicula pelicula);
    Voto findByUsuarioAndJuego(Usuario usuario, Juego juego);

    List<Voto> findAllByUsuario(Usuario usuario);


    List<Voto> findByUsuarioAndJuegoIsNull(Usuario usuario);
    List<Voto> findByUsuarioAndPeliculaIsNull(Usuario usuario);

    List<Voto> findByJuego(Juego juego);
    List<Voto> findByPelicula(Pelicula pelicula);
    
    List<Voto> findAllByJuego(Juego juego);

}
