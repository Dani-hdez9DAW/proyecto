package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long>  {
    public List<Juego> findByPlataforma(String plataforma);
}
