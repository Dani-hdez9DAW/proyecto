package org.proyect.controller.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.proyect.domain.Categoria;
import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.repository.JuegoRepository;
import org.proyect.repository.PeliculaRepository;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/usuario/")
@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @GetMapping("r")
    public String r(ModelMap m) {
        List<Pelicula> pelicula = peliculaRepository.findAll();
        List<Juego> juego = juegoRepository.findAll();

        m.put("peliculas", pelicula);
        m.put("juegos", juego);

        m.put("view", "usuario/r");
        return "_t/frame";
    }

    // -------------------------NATALIA---------------------------------------

    @GetMapping("rAdmin")
    public String rUsuarios(ModelMap m, HttpSession s) {
        // Verifica si el usuario está autenticado como administrador
        if (H.isRolOk("admin", s)) {
            // Invocación del método para obtener la lista de usuarios
            List<Usuario> usuarios = usuarioService.findAll();
            m.put("usuarios", usuarios);
            m.put("view", "usuario/rAdmin");
            return "_t/frame"; // Retorna la vista para mostrar la lista de usuarios
        } else {
            // Si el usuario no está autenticado como administrador, redirige a la página de
            // inicio
            // Si el usuario no está autenticado como administrador, redirige a la página de
            // inicio
            return "redirect:/"; // Redirige a la página de inicio
        }
    }

    @GetMapping("rDetailed")
    public String rDetailed(ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            if (usuario != null) {
                // Añadir películas favoritas al modelo
                List<Pelicula> peliculasFavoritas = usuario.getPeliculasFav();
                m.put("peliculasFavoritas", peliculasFavoritas);

                // Añadir videojuegos favoritos al modelo
                List<Juego> juegosFavoritos = usuario.getJuegosFav();
                m.put("juegosFavoritos", juegosFavoritos);
            }

            m.put("usuario", usuario);
            m.put("view", "usuario/rDetailed");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio
            // de sesión u otra página apropiada.
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }

    }

    @GetMapping("obtenerPuntos")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> obtenerPuntos(HttpSession session) throws Exception {
        // Obtiene el nombre de usuario de la sesión
        String nombreUsuario = (String) session.getAttribute("nombre");

        // Obtiene los puntos del usuario desde el servicio
        int puntos = usuarioService.obtenerPuntos(nombreUsuario);

        System.out.println("Obteniendo puntos para el usuario: " + nombreUsuario);
        // Crea un mapa para almacenar los puntos y lo devuelve como respuesta en
        // formato JSON
        Map<String, Integer> response = new HashMap<>();
        response.put("puntos", puntos);

        return ResponseEntity.ok(response);
    }

    // EDITAR LA DESCRIPCIÓN
    @PostMapping("actualizarDescripcion")
    public ResponseEntity<String> actualizarDescripcion(@RequestParam("nuevaDescripcion") String nuevaDescripcion,
            HttpSession session) {
        // Obtener el nombre de usuario de la sesión
        String nombreUsuario = (String) session.getAttribute("nombre");

        // Verificar si el nombre de usuario está presente en la sesión
        if (nombreUsuario != null) {
            // Buscar el usuario en la base de datos por su nombre de usuario
            Usuario usuario = usuarioService.findByCorreo(nombreUsuario);

            // Verificar si se encontró el usuario en la base de datos
            if (usuario != null) {
                // Actualizar la descripción del usuario con la nueva descripción proporcionada
                usuario.setDescripcion(nuevaDescripcion);

                // Guardar los cambios en la base de datos
                usuarioService.actualizarDescripcion(nombreUsuario, nuevaDescripcion);

                // Devolver una respuesta exitosa
                return ResponseEntity.ok("Descripción actualizada correctamente");
            } else {
                // Si no se encuentra el usuario, devolver un error
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
        } else {
            // Si no se encuentra el nombre de usuario en la sesión, devolver un error
            return ResponseEntity.badRequest().body("Usuario no autenticado");
        }
    }

    // Mostrar el número de películas
    @GetMapping("cantidadPeliculasFavoritas")
    @ResponseBody
    public ResponseEntity<Integer> getCantidadPeliculasFavoritas(HttpSession session) {
        // Obtener el nombre de usuario de la sesión
        String nombreUsuario = (String) session.getAttribute("nombre");

        // Verificar si el nombre de usuario está presente en la sesión
        if (nombreUsuario != null) {
            // Llamar al servicio para obtener la cantidad de películas favoritas del
            // usuario
            int cantidadPeliculasFavoritas = usuarioService.obtenerCantidadPeliculasFavoritas(nombreUsuario);

            // Devolver el número de películas favoritas del usuario
            return ResponseEntity.ok(cantidadPeliculasFavoritas);
        }

        // Si no se puede obtener el número de películas favoritas, devolver un error
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        return ResponseEntity.ok(0);
    }

    // Mostrar el número de juegos
    @GetMapping("cantidadJuegosFavoritos")
    @ResponseBody
    public ResponseEntity<Integer> getCantidadJuegosFavoritos(HttpSession session) {
        // Obtener el nombre de usuario de la sesión
        String nombreUsuario = (String) session.getAttribute("nombre");

        // Verificar si el nombre de usuario está presente en la sesión
        if (nombreUsuario != null) {
            // Llamar al servicio para obtener la cantidad de películas favoritas del
            // usuario
            int cantidadJuegosFavoritos = usuarioService.obtenerCantidadJuegosFavoritos(nombreUsuario);

            // Devolver el número de películas favoritas del usuario
            return ResponseEntity.ok(cantidadJuegosFavoritos);
        }

        // Si no se puede obtener el número de películas favoritas, devolver un error
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        return ResponseEntity.ok(0);
    }
    // ----------------------------------------------------------------

    @GetMapping("u")
    public String update(@RequestParam("id") String email,
            ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Si el usuario está autenticado, continúa con la lógica para cargar la vista
            // rDetailed
            m.put("usuario", usuarioService.findByCorreo(email));
            System.out.println("" + usuarioService.findByCorreo(email));
            m.put("view", "usuario/u");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
            return "redirect:/"; // Cambia "/login" por la ruta correcta de tu página de inicio de sesión
        }
    }

    @GetMapping("c")
    public String c(ModelMap m) {
        m.put("view", "usuario/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("pass") String password,
            @RequestParam("email") String correo) throws DangerException, InfoException {
        String mensaje = "El usuario con el nombre " + nombre;
        Boolean creado = false;
        try {
            if (usuarioService.findByCorreo(correo) != null) {
                PRG.error("El correo electrónico " + correo + " ya está registrado", "/");
                return "redirect:/";
            }
            usuarioService.save(nombre, password, correo);
            creado = true;
        } catch (Exception e) {
            PRG.error("Error al crear el usuario", "/");
        }

        if (creado) {
            PRG.info(mensaje + " ha sido creado", "/");
        }

        return "redirect:/";
    }

    // @PostMapping("/uploadFoto")
    // public ResponseEntity<String> uploadFoto(@RequestParam("file") MultipartFile
    // file, HttpSession session) {
    // Usuario usuario = (Usuario) session.getAttribute("usuario");
    // Long id = (Long) session.getAttribute("id");
    // if (usuario != null) {
    // try {
    // usuarioService.saveFoto(id, file);
    // return ResponseEntity.ok("Foto subida exitosamente");
    // } catch (IOException e) {
    // return ResponseEntity.status(500).body("Error subiendo la foto");
    // }
    // } else {
    // return ResponseEntity.status(401).body("Usuario no autenticado");
    // }
    // // }
    // @PostMapping("/uploadFotoPerfil")
    // public ResponseEntity<String> uploadFotoPerfil(@RequestParam("file") MultipartFile file, HttpSession session) {
    //     try {
    //         Usuario usuario = (Usuario) session.getAttribute("usuario");
    //         if (usuario != null) {
    //             // Obtener el ID del usuario
    //             Long usuarioId = usuario.getIdPersona();
    //             // Guardar la foto de perfil
    //             usuarioService.saveFotoPerfil(usuarioId, file);
    //             return ResponseEntity.ok("Foto subida exitosamente");
    //         } else {
    //             return ResponseEntity.badRequest()
    //                     .body("No se encontró ningún usuario con el correo electrónico proporcionado");
    //         }
    //     } catch (IOException e) {
    //         return ResponseEntity.status(500).body("Error subiendo la foto");
    //     }
    // }

}
