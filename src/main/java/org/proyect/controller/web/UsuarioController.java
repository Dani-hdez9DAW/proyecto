package org.proyect.controller.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.proyect.domain.Categoria;
import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.ComentarioValidator;
import org.proyect.helper.EmailSenderService;
import org.proyect.helper.EmailValidator;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.repository.JuegoRepository;
import org.proyect.repository.PeliculaRepository;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private JavaMailSender javaMailSender;

    // @GetMapping("r")
    // public String r(ModelMap m) {
    // List<Pelicula> pelicula = peliculaRepository.findAll();
    // List<Juego> juego = juegoRepository.findAll();

    // m.put("peliculas", pelicula);
    // m.put("juegos", juego);

    // m.put("view", "usuario/r");
    // return "_t/frame";
    // }

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

                // Poner la lista de películas favoritas en el modelo para que esté disponible
                // en la vista
                m.put("peliculasFavoritas", peliculasFavoritas);
            }

            m.put("usuario", usuario);
            // m.put("cantidadPeliculasFavoritas", cantidadPeliculasFavoritas);
            m.put("view", "usuario/rDetailed");
            return "_t/frame";
        } else {
            return "redirect:/";
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
            PRG.error("Formato de correo electrónico no válido");
            return "redirect:/"; // Redirigir en caso de error de formato
        }
        if (!ComentarioValidator.validarComentario(nombre)) {
            PRG.error("El nombre tiene palabras prohibidas", "/");
        }
        if (!ComentarioValidator.validarComentario(correo)) {
            PRG.error("El email tiene palabras prohibidas", "/");
        }

        // Validar el nombre y la contraseña
        if (!EmailValidator.isValidNamePass(nombre, password)) {
            PRG.error("Nombre o contraseña no válidos");
            return "redirect:/"; // Redirigir si la validación de nombre/contraseña falla
        }

        // Guardar el usuario
        usuarioService.save(nombre, password, correo);
        sendEmail(correo, "Bienvenido a nuestra pagina de videojuegos y peliculas", contenido);

        // EmailSender.sendEmail(correo, "Bienvenido a la plataforma de videojuegos",
        // "Que tengas un buen dia");
        // Informar al usuario si se creó correctamente
        PRG.info("El usuario con el nombre " + nombre + " ha sido creado", "/");

        return "redirect:/";
    }

    @PostMapping("cAdmin")
    public String cAdminPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String password,
            @RequestParam("correo") String correo,
            @RequestParam("roleOptions") String rolSeleccionado, HttpSession session)
            throws Exception {
        // String contenido = "Querido/a " + nombre + ",\n\n" +
        // "Gracias por registrarte en nuestra página de películas y videojuegos.\n" +
        // "En nuestra plataforma, encontrarás una amplia selección de películas y
        // videojuegos para disfrutar.\n" +
        // "No dudes en explorar nuestras secciones y descubrir contenido que te
        // encante.\n" +
        // "Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto
        // con nosotros.\n\n" +
        // "Un saludo,\n" +
        // "Equipo de películas y videojuegos.📽️🎮";// La imagen será incluida mediante
        // un identificador

        // Validar el formato del correo electrónico
        if (!EmailValidator.isValidEmail(correo)) {
            PRG.error("Formato de correo electrónico no válido");
            return "redirect:/"; // Redirigir en caso de error de formato
        }
        if (!ComentarioValidator.validarComentario(nombre)) {
            PRG.error("El nombre tiene palabras prohibidas", "/");
        }
        if (!ComentarioValidator.validarComentario(correo)) {
            PRG.error("El email tiene palabras prohibidas", "/");
        }

        // Validar el nombre y la contraseña
        if (!EmailValidator.isValidNamePass(nombre, password)) {
            PRG.error("Nombre o contraseña no válidos");
            return "redirect:/"; // Redirigir si la validación de nombre/contraseña falla
        }

        // Guardar el usuario
        usuarioService.saveAdmin(nombre, password, correo, rolSeleccionado);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String correoUsuario = usuario.getCorreo();
        usuarioService.modificacionPuntos(correoUsuario, 8);
        // sendEmail(correo, "Bienvenido a nuestra pagina de videojuegos y peliculas",
        // contenido);

        // EmailSender.sendEmail(correo, "Bienvenido a la plataforma de videojuegos",
        // "Que tengas un buen dia");
        // Informar al usuario si se creó correctamente
        PRG.info("El usuario con el nombre " + nombre + " ha sido creado", "/");

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
        PRG.info("Los permisos del usuario se han actualizado", "/usuario/rAdmin");

        return "redirect:/usuario/rAdmin";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("idPersona") Long idPersona) throws DangerException {
        try {
            usuarioService.delete(idPersona);
        } catch (Exception e) {
            PRG.error("", "/usuario/r");
        }
        return "redirect:/usuario/r";
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
