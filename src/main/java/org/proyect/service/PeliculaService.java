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

    public void save(String titulo, List<Long> categoriaIds, String clasificacion, Integer duracion, String estado,
            String plataforma,
            String sinopsis, LocalDate fechaLanzamiento, String imagen, String trailer, String urlCompra) {
        Pelicula pelicula = new Pelicula(titulo, clasificacion, duracion, estado, plataforma, sinopsis,
                fechaLanzamiento, trailer, urlCompra);
        pelicula.setImagen(imagen);

        // Inicializar la lista de categorías para evitar NullPointerException
        List<Categoria> categorias = new ArrayList<>();
        if (categoriaIds != null) {
            // Obtener y agregar las categorías a la lista
            for (Long categoriaId : categoriaIds) {
                Categoria categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriaId));
                categorias.add(categoria);
            }
        }
        // Establecer las categorías en la película
        pelicula.setCategorias(categorias);

        // Guardar la película
        peliculaRepository.save(pelicula);
    }

    public List<Pelicula> findByTitulo(String titulo) {
        return peliculaRepository.findByTitulo(titulo);
    }

    public Pelicula findByIdElemento(Long elementoId) {
        return peliculaRepository.findById(elementoId).get();
    }

    public void update(Long idPelicula, String titulo, String clasificacion, Integer duracion,
            String estado, String plataforma, String sinopsis, LocalDate fechaLanzamiento, Integer cuentaVotos,
            String trailer, String url) {
        Pelicula pelicula = peliculaRepository.findById(idPelicula).get();
        pelicula.setTitulo(titulo);
        pelicula.setClasificacion(clasificacion);
        pelicula.setDuracion(duracion);
        pelicula.setEstado(estado);
        pelicula.setPlataforma(plataforma);
        pelicula.setSinopsis(sinopsis);
        pelicula.setFecha_salida(fechaLanzamiento);
        pelicula.setCuenta_votos(cuentaVotos);
        pelicula.setTrailer(trailer);
        pelicula.setUrl(url);
        pelicula.setCategorias(null);
        peliculaRepository.save(pelicula);
    }

    public void delete(Long idPelicula) {
        peliculaRepository.delete(peliculaRepository.getReferenceById(idPelicula));
    }
    
}
