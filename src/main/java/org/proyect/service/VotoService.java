package org.proyect.service;

import java.util.List;

import org.proyect.domain.Evento;
import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.domain.Voto;
import org.proyect.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    public Voto votarPelicula(Usuario usuario, Pelicula pelicula, Long puntaje) {
        // Buscar si el usuario ya tiene un voto para esta película
        Voto votoExistente = votoRepository.findByUsuarioAndPelicula(usuario, pelicula);
        Voto votoGuardado;
        if (votoExistente != null) {
            // Si ya existe un voto, actualizar el puntaje
            votoExistente.setPuntaje(puntaje);
            votoGuardado = votoRepository.save(votoExistente);
        } else {
            // Si no existe un voto, crear uno nuevo
            Voto nuevoVoto = new Voto(usuario, pelicula, puntaje);
            votoGuardado = votoRepository.save(nuevoVoto);
        }
        return votoGuardado;
    }

    public Voto votarJuego(Usuario usuario, Juego juego, Long puntaje) {
        // Buscar si el usuario ya tiene un voto para esta juego
        Voto votoExistente = votoRepository.findByUsuarioAndJuego(usuario, juego);
        Voto votoGuardado;
    
        if (votoExistente != null) {
            // Si ya existe un voto, actualizar el puntaje
            votoExistente.setPuntaje(puntaje);
            System.out.println("luhflhdf" + votoExistente.getIdVoto());
            votoGuardado = votoRepository.save(votoExistente);
        } else {
            // Si no existe un voto, crear uno nuevo
            Voto nuevoVoto = new Voto(usuario, juego, puntaje);
            votoGuardado = votoRepository.save(nuevoVoto);
        }
        return votoGuardado;
    }

    public Voto findById(Long votoId) {
        return votoRepository.findById(votoId).orElse(null);
    }

    public List<Voto> findAllByUsuario(Usuario usuario) {
        return votoRepository.findAllByUsuario(usuario);
    }

    public List<Voto> findAll() {
        return votoRepository.findAll();
    }

    public List<Voto> findAllByUsuarioAndJuegoIsNull(Usuario usuario) {
        return votoRepository.findByUsuarioAndJuegoIsNull(usuario);
    }

    public List<Voto> findAllByUsuarioAndPeliculaIsNull(Usuario usuario) {
        return votoRepository.findByUsuarioAndPeliculaIsNull(usuario);
    }

    public List<Voto> findByJuego(Juego juego) {
        return votoRepository.findByJuego(juego);
    }

    public List<Voto> findByPelicula(Pelicula pelicula) {
        return votoRepository.findByPelicula(pelicula);
    }

    public Voto findByUsuarioAndJuego(Usuario usuario, Juego juego) {
        return votoRepository.findByUsuarioAndJuego(usuario, juego);
    }

    public Voto findByUsuarioAndPelicula(Usuario usuario, Pelicula pelicula) {
        return votoRepository.findByUsuarioAndPelicula(usuario, pelicula);
    }

    public Voto save(Voto nuevoVoto) {
        return votoRepository.save(nuevoVoto);
    }
    

    public List<Voto> findAllByJuego(Juego juego) {
        return votoRepository.findByJuego(juego);
    }
    public List<Voto> findAllByPelicula(Pelicula pelicula) {
        return votoRepository.findByPelicula(pelicula);
    }
    

}
