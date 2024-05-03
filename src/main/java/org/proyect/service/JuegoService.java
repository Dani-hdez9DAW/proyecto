package org.proyect.service;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {
    @Autowired
    private JuegoRepository juegoRepository;

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public void save(String nombre) {
        Juego Juego = new Juego(null, nombre);
        juegoRepository.save(Juego);
    }

    public Juego findById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    public List<Juego> findByTitulo(String titulo) {
        return juegoRepository.findByTitulo(titulo);
    }

    public Juego findByIdElemento(Long elementoId) {
        return juegoRepository.findById(elementoId).get();
    }
}
