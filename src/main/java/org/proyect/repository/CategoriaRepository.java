package org.proyect.repository;
 

import java.util.List;

import org.proyect.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    public List<Categoria> findByNombre(String nombre);
    
} 
