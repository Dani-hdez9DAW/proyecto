package org.proyect.controller.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.domain.Voto;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.ComentarioValidator;
import org.proyect.helper.EmailValidator;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.UsuarioService;
import org.proyect.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/usuario/")
@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VotoService votoService;

    @Autowired
    private JavaMailSender javaMailSender;

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
        if (H.isRolOk("auth", session)) {
            // Obtener el usuario actual desde el servicio
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Verificar si el usuario existe y tiene películas favoritas
            if (usuario != null && usuario.getPeliculasFav() != null && !usuario.getPeliculasFav().isEmpty()) {
                // Obtener la lista de películas favoritas del usuario
                List<Pelicula> peliculasFavoritas = usuario.getPeliculasFav();
                List<Juego> juegosFavoritos = usuario.getJuegosFav();
                int canPelis = peliculasFavoritas.size();
                int canJuegos = juegosFavoritos.size();
                // Poner la lista de películas favoritas en el modelo para que esté disponible
                // en la vista
                m.put("cantP", canPelis);
                m.put("cantJ", canJuegos);
                m.put("puntos", usuario.getPuntos());
                m.put("peliculasFavoritas", peliculasFavoritas);
                m.put("juegosFavoritos", juegosFavoritos);
            }

            m.put("usuario", usuario);
            // m.put("cantidadPeliculasFavoritas", cantidadPeliculasFavoritas);
            m.put("view", "usuario/rDetailed");
            return "_t/frame";
        } else {
            return "redirect:/";
        }
    }

    // @PostMapping("/guardarFotoPerfil")
    // @ResponseBody
    // public String guardarFotoPerfil(@RequestParam("imageUrl") String
    // imageUrl,HttpSession session) {
    // // Guarda la URL de la foto de perfil en la sesión del usuario
    // session.setAttribute("profileImageUrl", imageUrl);
    // return "Foto de perfil guardada en la sesión del usuario";
    // }

    // @GetMapping("/perfil")
    // public String mostrarPerfil(ModelMap model, HttpSession session) {
    // // Obtiene la URL de la foto de perfil de la sesión del usuario
    // String profileImageUrl = (String) session.getAttribute("profileImageUrl");
    // // Agrega la URL de la foto de perfil al modelo para que esté disponible en
    // la vista
    // model.addAttribute("profileImageUrl", profileImageUrl);
    // // Retorna la vista del perfil
    // return "perfil";
    // }

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

    // // Mostrar el número de juegos
    // @GetMapping("cantidadJuegosFavoritos")
    // @ResponseBody
    // public ResponseEntity<Integer> getCantidadJuegosFavoritos(HttpSession
    // session) {
    // // Obtener el nombre de usuario de la sesión
    // Usuario usuario = (Usuario) session.getAttribute("usuario");
    // String nombreUsuario = (String) session.getAttribute("nombre");

    // // Verificar si el nombre de usuario está presente en la sesión
    // if (nombreUsuario != null) {
    // // Llamar al servicio para obtener la cantidad de películas favoritas del
    // // usuario
    // int cantidadJuegosFavoritos =
    // usuarioService.obtenerCantidadJuegosFavoritos(nombreUsuario);

    // // Devolver el número de películas favoritas del usuario
    // return ResponseEntity.ok(cantidadJuegosFavoritos);
    // }

    // // Si no se puede obtener el número de películas favoritas, devolver un error
    // // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // return ResponseEntity.ok(0);
    // }
    // ----------------------------------------------------------------

    @GetMapping("u")
    public String update(@RequestParam("id") Long id,
            ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Si el usuario está autenticado, continúa con la lógica para cargar la vista
            // rDetailed
            m.put("usuario", usuarioService.findById(id));
            m.put("view", "usuario/u");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
            return "redirect:/"; // Cambia "/login" por la ruta correcta de tu página de inicio de sesión
        }
    }

    @GetMapping("cAdmin")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) {
            m.put("view", "usuario/cAdmin");
            return "_t/frame";
        } else {
            return "redirect:/";
        }

    }

    public void sendEmail(String emailTo, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("pelijuegosdanaca33@gmail.com");

        javaMailSender.send(message);
        System.out.println("Correo enviado exitosamente");
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("pass") String password,
            @RequestParam("email") String correo) throws Exception {
        String contenido = "Querido/a " + nombre + ",\n\n" +
                "Gracias por registrarte en nuestra página de películas y videojuegos.\n" +
                "En nuestra plataforma, encontrarás una amplia selección de películas y videojuegos para disfrutar.\n" +
                "No dudes en explorar nuestras secciones y descubrir contenido que te encante.\n" +
                "Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto con nosotros.\n\n" +
                "Un saludo,\n" +
                "Equipo de películas y videojuegos.📽️🎮";// La imagen será incluida mediante un identificador

        // Validar el formato del correo electrónico
        if (!EmailValidator.isValidEmail(correo)) {
            // PRG.error("Formato de correo electrónico no válido");
            return "redirect:/"; // Redirigir en caso de error de formato
        }
        if (!ComentarioValidator.validarComentario(nombre)) {
            // PRG.error("El nombre tiene palabras prohibidas", "/");
        }
        if (!ComentarioValidator.validarComentario(correo)) {
            // PRG.error("El email tiene palabras prohibidas", "/");
        }

        // Validar el nombre y la contraseña
        if (!EmailValidator.isValidNamePass(nombre, password)) {
            // PRG.error("Nombre o contraseña no válidos");
            return "redirect:/"; // Redirigir si la validación de nombre/contraseña falla
        }

        // Guardar el usuario
        usuarioService.save(nombre, password, correo);
        sendEmail(correo, "Bienvenido a nuestra pagina de videojuegos y peliculas", contenido);

        // EmailSender.sendEmail(correo, "Bienvenido a la plataforma de videojuegos",
        // "Que tengas un buen dia");
        // Informar al usuario si se creó correctamente

        return "redirect:/usuario/rAdmin";
    }

    @PostMapping("cAdmin")
    public String cAdminPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String password,
            @RequestParam("correo") String correo,
            @RequestParam("roleOptions") String rolSeleccionado, HttpSession session)
            throws Exception {

        // Validar el formato del correo electrónico
        if (!EmailValidator.isValidEmail(correo)) {
            // PRG.error("Formato de correo electrónico no válido");
            return "redirect:/"; // Redirigir en caso de error de formato
        }
        if (!ComentarioValidator.validarComentario(nombre)) {
            // PRG.error("El nombre tiene palabras prohibidas", "/");
        }
        if (!ComentarioValidator.validarComentario(correo)) {
            // PRG.error("El email tiene palabras prohibidas", "/");
        }

        // Validar el nombre y la contraseña
        if (!EmailValidator.isValidNamePass(nombre, password)) {
            // PRG.error("Nombre o contraseña no válidos");
            return "redirect:/"; // Redirigir si la validación de nombre/contraseña falla
        }

        // Guardar el usuario
        usuarioService.saveAdmin(nombre, password, correo, rolSeleccionado);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String correoUsuario = usuario.getCorreo();
        usuarioService.modificacionPuntos(correoUsuario, 8);

        // Informar al usuario si se creó correctamente
        // PRG.info("El usuario con el nombre " + nombre + " ha sido creado", "/");

        return "redirect:/";
    }

    @PostMapping("updateUser")
    public String updateUser(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String password,
            @RequestParam("correo") String correo, HttpSession session)
            throws Exception {

        // Validar el formato del correo electrónico
        if (!EmailValidator.isValidEmail(correo)) {
            // PRG.error("Formato de correo electrónico no válido");
            return "redirect:/"; // Redirigir en caso de error de formato
        }
        if (!ComentarioValidator.validarComentario(nombre)) {
            // PRG.error("El nombre tiene palabras prohibidas", "/");
        }
        if (!ComentarioValidator.validarComentario(correo)) {
            // PRG.error("El email tiene palabras prohibidas", "/");
        }

        // Validar el nombre y la contraseña
        if (!EmailValidator.isValidNamePass(nombre, password)) {
            // PRG.error("Nombre o contraseña no válidos");
            return "redirect:/"; // Redirigir si la validación de nombre/contraseña falla
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Long idUsuario = usuario.getIdPersona();
        // Guardar el usuario
        usuarioService.updateUser(idUsuario, nombre, password, correo);
        // PRG.info("Tu usuario ha sido actualizado", "/");

        return "redirect:/";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("roleOptions") String rolSeleccionado, HttpSession session)
            throws DangerException, InfoException {

        usuarioService.updatePermisos(idUsuario, rolSeleccionado);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String correoUsuario = usuario.getCorreo();
        usuarioService.modificacionPuntos(correoUsuario, 2);
        // PRG.info("Los permisos del usuario se han actualizado", "/usuario/rAdmin");

        return "redirect:/usuario/rAdmin";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("idPersona") Long idPersona) throws DangerException {
        try {
            usuarioService.delete(idPersona);
        } catch (Exception e) {
            // PRG.error("", "/usuario/r");
        }
        return "redirect:/usuario/r";
    }

    // @PostMapping("dPelicula")
    // public String dPelicula(
    // @RequestParam("idPersona") Long idPersona,
    // @RequestParam("idPelicula") Long idPelicula) throws DangerException {
    // try {
    // Usuario usuario = usuarioService.findById(idPersona);
    // Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);
    // List<Pelicula> peliculasUsuario = usuario.getPeliculasFav();

    // if (peliculasUsuario.contains(pelicula)) {
    // peliculasUsuario.remove(pelicula);
    // String nombre = usuario.getNombre();
    // String correo = usuario.getCorreo();
    // String contra = usuario.getContraseña();
    // usuarioService.save(nombre, correo, contra);// Save changes to the user
    // } else {
    // PRG.error("Película no encontrada en los favoritos del usuario",
    // "/usuario/rDetailed");
    // }
    // } catch (Exception e) {
    // PRG.error("Error al eliminar la película", "/usuario/rDetailed");
    // }
    // return "redirect:/usuario/rDetailed";
    // }

    @PostMapping("dJuego")
    public String dJuego(
            @RequestParam("idPersona") Long idPersona,
            @RequestParam("idjuego") Long idJuego) throws DangerException {
        Usuario usuario = usuarioService.findById(idPersona);
        usuarioService.eliminarJuegoFavorito(usuario, idJuego);

        // Redirigir al usuario a la página de perfil.
        return "redirect:/usuario/rDetailed";
    }

    @PostMapping("dPelicula")
    public String dPelicula(
            @RequestParam("idPersona") Long idPersona,
            @RequestParam("idPelicula") Long idPelicula,
            ModelMap m) throws DangerException {
        Usuario usuario = usuarioService.findById(idPersona);
        usuarioService.eliminarPeliculaFavorita(usuario, idPelicula);
        List<Pelicula> peliculasNuevas = usuario.getPeliculasFav();
        m.put("peliculasFavoritas", peliculasNuevas);

        // Redirigir al usuario a la página de perfil.
        return "redirect:/usuario/rDetailed";
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
    // public ResponseEntity<String> uploadFotoPerfil(@RequestParam("file")
    // MultipartFile file, HttpSession session) {
    // try {
    // Usuario usuario = (Usuario) session.getAttribute("usuario");
    // if (usuario != null) {
    // // Obtener el ID del usuario
    // Long usuarioId = usuario.getIdPersona();
    // // Guardar la foto de perfil
    // usuarioService.saveFotoPerfil(usuarioId, file);
    // return ResponseEntity.ok("Foto subida exitosamente");
    // } else {
    // return ResponseEntity.badRequest()
    // .body("No se encontró ningún usuario con el correo electrónico
    // proporcionado");
    // }
    // } catch (IOException e) {
    // return ResponseEntity.status(500).body("Error subiendo la foto");
    // }
    // }

}
