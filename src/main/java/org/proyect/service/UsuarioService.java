package org.proyect.service;

import java.util.List;

import org.proyect.domain.Usuario;
import org.proyect.domain._Bean;
import org.proyect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public Usuario save(String nombre) {
        return usuarioRepository.save(new Usuario(nombre));
    }

    public Usuario findById(Long id_Usuario) {
        return usuarioRepository.findById(id_Usuario).get();
    }
    public void update(Long id_Usuario, String nombre) {
        Usuario usuario = usuarioRepository.findById(id_Usuario).get();
        usuario.setNombre(nombre);
        usuarioRepository.save(usuario);
    }

    public void delete(Long id_Usuario) {
        usuarioRepository.delete(usuarioRepository.getReferenceById(id_Usuario));
    }
}
