package org.proyect.service;

import java.time.LocalDate;
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
        Juego Juego = new Juego(nombre);
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

     public void update(Long idJuego, String titulo, String clasificacion, Integer duracion,
            String estado, String plataforma, String sinopsis, LocalDate fechaLanzamiento, Integer cuentaVotos,
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
        juego.setCategorias(null);
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
