package org.proyect.repository;

import java.util.List;

import org.proyect.domain._Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface _BeanRepository extends JpaRepository<_Bean,Long> {
    public List<_Bean> findByNombre(String nombre);
}
