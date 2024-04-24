package org.proyect.service;

import java.util.List;

import org.proyect.domain.Actor;
import org.proyect.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    public List<Actor> findByNombre(String nombre) {
        return actorRepository.findByNombre(nombre);
    }

    public Actor save(String nombre) {
        return actorRepository.save(new Actor(nombre));
    }

    public Actor findById(Long id_Actor) {
        return actorRepository.findById(id_Actor).get();
    }

    public void update(Long id_Actor, String nombre) {
        Actor actor = actorRepository.findById(id_Actor).get();
        actor.setNombre(nombre);
        actorRepository.save(actor);
    }

    public void delete(Long id_Bean) {
        actorRepository.delete(actorRepository.getReferenceById(id_Bean));
    }
}
