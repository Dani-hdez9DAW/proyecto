package org.proyect.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
import org.proyect.helper.SistemaPuntuacion;
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

    public Usuario saveAdmin(String nombre, String passwd, String correo, String rolSeleccionado) {
        Boolean esAdmin = false;
        if ("admin".equals(rolSeleccionado)) {
            esAdmin = true;
        } else if ("user".equals(rolSeleccionado)) {
            esAdmin = false;
        }
        return usuarioRepository
                .save(new Usuario(nombre, (new BCryptPasswordEncoder()).encode(passwd), correo, 0, esAdmin));
    }

    public Usuario findById(Long id_Usuario) {
        return usuarioRepository.findById(id_Usuario).get();
    }

    public Usuario findByCorreo(String email) {
        return usuarioRepository.getByCorreo(email);
    }

    public void updatePermisos(Long id_Usuario, String rolSeleccionado) {
        // Verificar si el id del usuario no es nulo y es válido
        if (id_Usuario == null || !usuarioRepository.existsById(id_Usuario)) {
            throw new IllegalArgumentException("El ID de usuario no es válido.");
        }

        // Obtener el usuario desde el repositorio
        Usuario usuario = usuarioRepository.findById(id_Usuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Verificar si las contraseñas no son nulas, coinciden y no están vacías

        // Actualizar el rol del usuario
        if ("admin".equals(rolSeleccionado)) {
            usuario.setEsAdmin(true);
        } else if ("user".equals(rolSeleccionado)) {
            usuario.setEsAdmin(false);
        }

        // Guardar el usuario actualizado en el repositorio
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

    public void setRegistro(String email) throws DangerException {
        Usuario usuario = usuarioRepository.getByCorreo(email);
        if (usuario != null) {
            usuario.setEstaRegistrado(true);
            usuarioRepository.save(usuario);
        } else {
            // Manejar el caso en que el usuario no exista
            PRG.error("El usuario con nombre " + email + " no existe");
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

    public void modificacionPuntos(String correo, Integer puntuacionMaxima) {
        Usuario usuario = usuarioRepository.getByCorreo(correo);
        if (usuario != null) {
            Integer puntosUsuario = usuario.getPuntos();

            // Generar puntos aleatorios usando la clase PuntosAleatorios
            int puntosAleatorios = SistemaPuntuacion.generarPuntos(puntuacionMaxima);

            // Sumar los puntos aleatorios al usuario
            usuario.setPuntos(puntosUsuario + puntosAleatorios);
            usuarioRepository.save(usuario);
        }
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

    // // Método para mostrar las películas
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
            throw new UsernameNotFoundException("Usuario no encontrado: " +
                    nombreUsuario);
        }
    }

    // // Método para mostrar los videojuegos
    // public int obtenerCantidadJuegosFavoritos(String nombreUsuario) {
    // // Buscar al usuario por su nombre en la base de datos
    // Usuario usuario = usuarioRepository.getByNombre(nombreUsuario);

    // // Verificar si se encontró el usuario
    // if (usuario != null) {
    // // Obtener la lista de películas favoritas del usuario
    // List<Juego> juegosFavoritos = usuario.getJuegosFav();

    // // Devolver el tamaño de la lista de películas favoritas
    // return juegosFavoritos.size();
    // } else {
    // // Manejar el caso en que el usuario no se encuentre
    // throw new UsernameNotFoundException("Usuario no encontrado: " +
    // nombreUsuario);
    // }
    // }

    public Usuario removeUsuarioPeliculas(Usuario usuario, Pelicula pelicula) {
        List<Pelicula> peliculasFav = new ArrayList<>(usuario.getPeliculasFav()); // Usando ArrayList

        System.out.println("Antes de la eliminación: " + peliculasFav);

        if (peliculasFav.remove(pelicula)) {
            usuario.setPeliculasFav(peliculasFav);
            usuario = usuarioRepository.save(usuario);

            System.out.println("Después de la eliminación: " + usuario.getPeliculasFav());
        } else {
            throw new IllegalArgumentException("La película no está en la lista de favoritos.");
        }

        return usuario;
    }

    public Usuario removeUsuarioJuegos(Usuario usuario, Juego juego) {
        List<Juego> juegosFav = usuario.getJuegosFav();

        juegosFav.remove(juego);
        usuario.setJuegosFav(juegosFav);

        return usuarioRepository.save(usuario);
    }

    // public void saveFoto(Long id, MultipartFile file) throws IOException {
    // Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
    // if (usuarioOpt.isPresent()) {
    // Usuario usuario = usuarioOpt.get();
    // usuario.setFotoPerfil(file.getBytes());
    // usuarioRepository.save(usuario);
    // } else {
    // throw new RuntimeException("Usuario no encontrado");
    // }
    // }

    // public void saveFotoPerfil(Long id, MultipartFile file) throws IOException {
    // Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
    // if (usuarioOpt.isPresent()) {
    // Usuario usuario = usuarioOpt.get();
    // usuario.setFotoPerfil(file.getBytes());
    // usuarioRepository.save(usuario);
    // } else {
    // throw new RuntimeException("Usuario no encontrado");
    // }
    // }
}