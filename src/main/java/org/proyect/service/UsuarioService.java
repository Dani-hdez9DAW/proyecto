package org.proyect.service;

import java.util.List;

import org.proyect.domain.Usuario;
import org.proyect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public Usuario save(String nombre, String passwd) {
        return usuarioRepository.save(new Usuario(nombre, (new BCryptPasswordEncoder()).encode(passwd)));
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

    public void setAdmin(String nombre) {
        Usuario usuario = usuarioRepository.getByNombre(nombre);
        usuario.setEsAdmin(true);
        usuarioRepository.save(usuario);
    }

    public Usuario login(String nombre, String password) throws Exception {
        Usuario usuario = usuarioRepository.getByNombre(nombre);
        if (usuario == null) {
            throw new Exception("El nombre del usuario " + nombre + " no existe");
        }
        if (!(new BCryptPasswordEncoder()).matches(password, usuario.getContraseña())) {
            throw new Exception("La contraseña para el usuario " + nombre + " es incorrecta");
        }
        return usuario;
    }
}
