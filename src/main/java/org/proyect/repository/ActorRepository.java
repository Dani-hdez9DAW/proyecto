package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    public List<Actor> findByNombre(String nombre);
}
