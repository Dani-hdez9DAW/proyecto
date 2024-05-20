package org.proyect.service;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

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

    public Usuario getByNombre(String nombre) {
        return usuarioRepository.getByNombre(nombre);
    }

    public Usuario save(String nombre, String passwd, String correo) {
        return usuarioRepository.save(new Usuario(nombre, (new BCryptPasswordEncoder()).encode(passwd), correo, 0));
    }

    public Usuario findById(Long id_Usuario) {
        return usuarioRepository.findById(id_Usuario).get();
    }

    public Usuario findByCorreo(String email) {
        return usuarioRepository.getByCorreo(email);
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

    public Usuario login(String email, String password) throws Exception {
        Usuario usuario = usuarioRepository.getByCorreo(email);
        if (usuario == null) {
            throw new Exception("El nombre del usuario " + email + " no existe");
        }
        if (!(new BCryptPasswordEncoder()).matches(password, usuario.getContraseña())) {
            throw new Exception("La contraseña para el usuario " + email + " es incorrecta");
        }
        return usuario;
    }

    public void setRegistro(String email) {
        Usuario usuario = usuarioRepository.getByCorreo(email);
        if (usuario != null) {
            usuario.setEstaRegistrado(true);
            usuarioRepository.save(usuario);
        } else {
            // Manejar el caso en que el usuario no exista
            throw new IllegalArgumentException("El usuario con nombre " + email + " no existe");
        }
    }

    public void setLogout(String email) {
        Usuario usuario = usuarioRepository.getByCorreo(email);
        if (usuario != null) {
            usuario.setEstaRegistrado(false);
            usuarioRepository.save(usuario);
        } else {
            // Manejar el caso en que el usuario no exista
            throw new IllegalArgumentException("El usuario con nombre " + email + " no existe");
        }
    }

    public Usuario saveUsuarioPeliculas(Usuario usuario, Pelicula pelicula) {
        List<Pelicula> peliculasFav = usuario.getPeliculasFav();

        peliculasFav.add(pelicula);
        usuario.setPeliculasFav(peliculasFav);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario saveUsuarioJuegos(Usuario usuario, Juego juego) {
        List<Juego> juegosFav = usuario.getJuegosFav();

        juegosFav.add(juego);
        usuario.setJuegosFav(juegosFav);

        return usuarioRepository.save(usuario);
    }

    public int obtenerPuntos(String nombreUsuario) throws Exception {
        // Buscar el usuario por su nombre en la base de datos
        List<Usuario> usuarios = usuarioRepository.findByNombre(nombreUsuario);

        // Verificar si el usuario existe
        if (!usuarios.isEmpty()) {
            // Obtener el primer usuario de la lista (asumiendo que el nombre de usuario es
            // único)
            Usuario usuario = usuarios.get(0);
            // Obtener los puntos del usuario y devolverlos
            return usuario.getPuntos();
        } else {
            // Manejar el caso en el que el usuario no exista
            throw new Exception("El usuario no existe: " + nombreUsuario);
        }
    }

    public void actualizarDescripcion(String correoUsuario, String nuevaDescripcion) {
        // Buscar el usuario por su correo en la base de datos
        Usuario usuario = usuarioRepository.getByCorreo(correoUsuario);

        // Verificar si se encontró el usuario
        if (usuario != null) {
            // Actualizar la descripción del usuario con la nueva descripción proporcionada
            usuario.setDescripcion(nuevaDescripcion);

            // Guardar los cambios en la base de datos
            usuarioRepository.save(usuario);
        } else {
            // Manejar el caso en el que no se encuentre el usuario
            throw new UsernameNotFoundException("Usuario no encontrado: " + correoUsuario);
        }
    }

    // Método para mostrar las películas
    public int obtenerCantidadPeliculasFavoritas(String nombreUsuario) {
        // Buscar al usuario por su nombre en la base de datos
        Usuario usuario = usuarioRepository.getByNombre(nombreUsuario);

        // Verificar si se encontró el usuario
        if (usuario != null) {
            // Obtener la lista de películas favoritas del usuario
            List<Pelicula> peliculasFavoritas = usuario.getPeliculasFav();

            // Devolver el tamaño de la lista de películas favoritas
            return peliculasFavoritas.size();
        } else {
            // Manejar el caso en que el usuario no se encuentre
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
        }
    }

    // Método para mostrar los videojuegos
    public int obtenerCantidadJuegosFavoritos(String nombreUsuario) {
        // Buscar al usuario por su nombre en la base de datos
        Usuario usuario = usuarioRepository.getByNombre(nombreUsuario);

        // Verificar si se encontró el usuario
        if (usuario != null) {
            // Obtener la lista de películas favoritas del usuario
            List<Juego> juegosFavoritos = usuario.getJuegosFav();

            // Devolver el tamaño de la lista de películas favoritas
            return juegosFavoritos.size();
        } else {
            // Manejar el caso en que el usuario no se encuentre
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario);
        }
    }
}