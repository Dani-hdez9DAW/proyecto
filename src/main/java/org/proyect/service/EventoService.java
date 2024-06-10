package org.proyect.service;

import java.time.LocalDate;
import java.util.List;

import org.proyect.domain.Evento;
import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
import org.proyect.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public List<Evento> findByTipo(String tipo) {
        return eventoRepository.findByTipo(tipo);
    }

    public Evento save(Evento nuevoEvento) {
        return eventoRepository.save(nuevoEvento);
    }

    public Evento findById(Long idEvento) {
        return eventoRepository.findById(idEvento).orElse(null);
    }

    public void update(Long idEvento, String nombre, String localizacion, String tipo, LocalDate fechaEvento)
            throws DangerException {
        Evento evento = eventoRepository.findById(idEvento).orElse(null);
        if (evento == null) {
            PRG.error("El id del evento no existe o no se encuentra", "/evento/rAdmin");
        }
        evento.setNombre(nombre);
        evento.setLocalizacion(localizacion);
        evento.setTipo(tipo);
        evento.setFecha(fechaEvento);
        eventoRepository.save(evento);
    }

    public void delete(Long idEvento) {
        eventoRepository.delete(eventoRepository.getReferenceById(idEvento));
    }

    public List<String> findAllTipos() {
        return eventoRepository.findAllTipos();
    }
}