package org.proyect.service;

import java.time.LocalDate;
import java.util.List;

import org.proyect.domain.Comentario;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Juego;
import org.proyect.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PeliculaService peliculaService;
    
    @Autowired
    private JuegoService juegoService;

    public List<Comentario> findAll() {
        return comentarioRepository.findAll();
    }

    public List<Comentario> findByPeliculaComentarios(Long id) {
        Pelicula pelicula = peliculaService.findByIdElemento(id);
        return pelicula.getComentarios();
    }

    public List<Comentario> findByJuegoComentarios(Long id) {
        Juego juego = juegoService.findByIdElemento(id);
        return juego.getComentarios();
    }


    public Comentario savePelicula(String autor, String contenido, LocalDate fecha, Long idPelicula) {
        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);
        return comentarioRepository.save(new Comentario(autor, contenido, fecha, pelicula));
    }

    public Comentario saveJuego(String autor, String contenido, LocalDate fecha, Long idJuego) {
        Juego juego = juegoService.findByIdElemento(idJuego);
        return comentarioRepository.save(new Comentario(autor, contenido, fecha, juego));
    }

    public Comentario findById(Long id_Comentario) {
        return comentarioRepository.findById(id_Comentario).get();
    }

    public void update(Long id_Comentario, String nombre) {
        Comentario comentario = comentarioRepository.findById(id_Comentario).get();
        comentarioRepository.save(comentario);
    }

    public void delete(Long id_Comentario) {
        comentarioRepository.delete(comentarioRepository.getReferenceById(id_Comentario));
    }
}
