package org.proyect.service;

import java.util.List;

import org.proyect.domain.Pelicula;
import org.proyect.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeliculaService {
    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> findAll() {
        return peliculaRepository.findAll();
    }

    public void save(String nombre) {
        Pelicula pelicula = new Pelicula(null, nombre);

        peliculaRepository.save(pelicula);

    }

    public Pelicula findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
