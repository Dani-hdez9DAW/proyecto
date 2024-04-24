package org.proyect.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.domain.Pelicula;
import org.proyect.repository.CategoriaRepository;
import org.proyect.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PeliculaService {
    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Pelicula> findAll() {
        return peliculaRepository.findAll();
    }

    public void save(String titulo, List<Long> categoria, String clasificacion, Integer duracion, String estado,String plataforma,
            String sinopsis,LocalDate fechaLanzamiento, String imagen, String trailer, String urlCompra) {
        Pelicula pelicula = new Pelicula(titulo,clasificacion,duracion,estado,plataforma,sinopsis,fechaLanzamiento,imagen,trailer,urlCompra);
        categoria = (categoria == null ? new ArrayList<Long>() : categoria);
        for (Long categorias : categoria) {
            Categoria category = categoriaRepository.findById(categorias).get();
            pelicula.getCategorias().add(category);
        }
        peliculaRepository.save(pelicula);

    }

    public List<Pelicula> findByTitulo(String titulo) {
        return peliculaRepository.findByTitulo(titulo);
    }

    public Pelicula findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
}
