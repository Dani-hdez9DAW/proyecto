package org.proyect.service;

import java.util.List;

import org.proyect.domain.Juego;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
