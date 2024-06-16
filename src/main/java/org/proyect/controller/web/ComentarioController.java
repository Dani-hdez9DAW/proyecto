package org.proyect.controller.web;

import java.time.LocalDate;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.helper.ComentarioValidator;
import org.proyect.helper.PRG;
import org.proyect.service.ComentarioService;
import org.proyect.service.JuegoService;
import org.proyect.service.PeliculaService;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/comentario")
@Controller
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private JuegoService juegoService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("r")
    public String r(ModelMap m) {
        m.put("comentarios", comentarioService.findAll());
        m.put("view", "comentario/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        m.put("view", "comentario/c");
        return "_t/frame";
    }

    @PostMapping("cP")
    public String cPostPelicula(
            @RequestParam("autor") String autor,
            @RequestParam("contenido") String contenido,
            @RequestParam("idPelicula") Long idPelicula,
            HttpSession session,
            ModelMap m) {
        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);

        // Si no contiene palabras malsonantes, guarda el comentario en la base de datos
        try {
            if (ComentarioValidator.validarComentario(contenido)) {
                // GESTION DE PUNTOS DEL USUARIO
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                String correoUsuario = usuario.getCorreo();
                usuarioService.modificacionPuntos(correoUsuario, 4);

                comentarioService.savePelicula(autor, contenido, LocalDate.now(), idPelicula);
                // PRG.info("Comentario guardado", "redirect:/pelicula/rDetailed?id_elemento=" +
                // pelicula.getIdElemento());
            } else {
                // PRG.error("El comentario contiene palabras
                // malsonantes.","redirect:/pelicula/rDetailed?id_elemento=" +
                // pelicula.getIdElemento());
            }

        } catch (Exception e) {
            // Manejar la excepción adecuadamente, por ejemplo, mostrar un mensaje de error
        }

        return "redirect:/pelicula/rDetailed?id_elemento=" + pelicula.getIdElemento();
    }

    @PostMapping("cJ")
    public String cPostJuego(
            @RequestParam("autor") String autor,
            @RequestParam("contenido") String contenido,
            @RequestParam("idJuego") Long idJuego,
            HttpSession session,
            ModelMap m) {
        Juego juego = juegoService.findByIdElemento(idJuego);

        // Si no contiene palabras malsonantes, guarda el comentario en la base de datos
        try {
            if (ComentarioValidator.validarComentario(contenido)) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                String correoUsuario = usuario.getCorreo();
                usuarioService.modificacionPuntos(correoUsuario, 4);
                comentarioService.saveJuego(autor, contenido, LocalDate.now(), idJuego);
                PRG.info("Comentario guardado", "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento());
            } else {
                PRG.error("El comentario contiene palabras malsonantes.",
                        "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento());
            }
        } catch (Exception e) {
            // Manejar la excepción adecuadamente, por ejemplo, mostrar un mensaje de error
        }

        return "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento();
    }

    @GetMapping("u")
    public String u(
            @RequestParam("id") Long idComentario,
            ModelMap m) {
        m.put("comentario", comentarioService.findById(idComentario));
        m.put("view", "comentario/u");
        return "_t/frame";
    }

    @PostMapping("d")
    public String d(@RequestParam("id") Long idComentario) {
        try {
            comentarioService.delete(idComentario);
        } catch (Exception e) {
            // Manejar la excepción adecuadamente, por ejemplo, mostrar un mensaje de error
        }
        return "redirect:/comentario/r";
    }
}
