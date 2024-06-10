package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    public List<Evento> findByTipo(String tipo);

    @Query("SELECT DISTINCT e.tipo FROM Evento e")
    List<String> findAllTipos();
}