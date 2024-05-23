package org.proyect.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.domain.Juego;
import org.proyect.repository.CategoriaRepository;
import org.proyect.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {
    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public Page<Juego> findAll(Pageable pageable) {
        return juegoRepository.findAll(pageable);
    }

    public void save(String titulo, List<Long> categoriaIds, String clasificacion, Integer duracion, Integer puntuacion,
            String estado,
            String plataforma,
            String sinopsis, LocalDate fechaLanzamiento, String imagen, String trailer, String urlCompra) {
        Juego juego = new Juego(titulo, clasificacion, duracion, puntuacion, estado, plataforma, sinopsis,
                fechaLanzamiento, trailer, urlCompra);
        juego.setImagen(imagen);
        juego.setCuenta_votos(0);
        if (fechaLanzamiento.isBefore(LocalDate.now())) {
            juego.setEstado("Unreleased");
        }

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
        juego.setCategorias(categorias);

        // Guardar la película
        juegoRepository.save(juego);
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

    public void update(Long idJuego, String titulo, String clasificacion, Integer duracion,
            String estado, String plataforma, Integer puntuacion, List<Long> idsCategoria, String sinopsis,
            LocalDate fechaLanzamiento, Integer cuentaVotos,
            String trailer, String url) {
        Juego juego = juegoRepository.findById(idJuego).get();
        juego.setTitulo(titulo);
        juego.setClasificacion(clasificacion);
        juego.setDuracion(duracion);
        juego.setEstado(estado);
        juego.setPlataforma(plataforma);
        juego.setSinopsis(sinopsis);
        juego.setFecha_salida(fechaLanzamiento);
        juego.setCuenta_votos(cuentaVotos);
        juego.setTrailer(trailer);
        juego.setUrl(url);
        juego.setPuntuacion(puntuacion);
        if (fechaLanzamiento.isBefore(LocalDate.now())) {
            juego.setEstado("Unreleased");
        }

        idsCategoria = (idsCategoria == null) ? new ArrayList<Long>() : idsCategoria;
        juego.getCategorias().clear();
        List<Categoria> nuevosCategorias = new ArrayList<Categoria>();
        for (Long idCategoria : idsCategoria) {
            Categoria categoria = categoriaRepository.findById(idCategoria).get();
            nuevosCategorias.add(categoria);
        }
        juego.setCategorias(nuevosCategorias);
        juegoRepository.save(juego);
    }

    public void delete(Long idJuego) {
        juegoRepository.delete(juegoRepository.getReferenceById(idJuego));
    }

    public Long setCalificacion(Juego juego, Long puntos) {
        if (puntos != null) {
            // Agregar la calificación a la lista de la película específica
            juego.getCalificaciones().add(puntos);
            int conteoVotos = juego.getCalificaciones().size();
            Long cali = (long) calcularCalificacion(juego.getCalificaciones(), conteoVotos);
            juego.setCalificacion(cali);
            juegoRepository.save(juego);
            return cali;
        } else {
            puntos = 0L;
            // Agregar la calificación a la lista de la película específica
            juego.getCalificaciones().add(puntos);
            int conteoVotos = juego.getCalificaciones().size();
            Long cali = (long) calcularCalificacion(juego.getCalificaciones(), conteoVotos);
            juego.setCalificacion(cali);
            juegoRepository.save(juego);
            return cali;
        }
    }

    private double calcularCalificacion(List<Long> calificaciones, int conteoVotos) {
        long sumaCalificaciones = 0;
        for (Long calificacion : calificaciones) {
            sumaCalificaciones += calificacion;
        }
        return (double) sumaCalificaciones / conteoVotos;
    }

}
