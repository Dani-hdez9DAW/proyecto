package org.proyect.repository;

import java.util.List;

import org.proyect.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public List<Usuario> findByNombre(String nombre);

    public Usuario getByNombre(String nombre);
}
