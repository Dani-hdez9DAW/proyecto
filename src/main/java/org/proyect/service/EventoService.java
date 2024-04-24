package org.proyect.service;

import java.util.List;

import org.proyect.domain.Evento;
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

    public List<Evento> findByTipo(String nombre) {
        return eventoRepository.findByTipo(nombre);
    }

    public Evento save(String nombre) {
        return eventoRepository.save(new Evento(nombre, null, nombre));
    }

    public Evento findById(Long idEvento) {
        return eventoRepository.findById(idEvento).get();
    }

    public void update(Long idEvento, String nombre) {
        Evento Evento = eventoRepository.findById(idEvento).get();
        Evento.setTipo(nombre);
        eventoRepository.save(Evento);
    }

    public void delete(Long idEvento) {
        eventoRepository.delete(eventoRepository.getReferenceById(idEvento));
    }
}
