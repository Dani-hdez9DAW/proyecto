package org.proyect.controller.web;

import java.time.LocalDate;
import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.JuegoService;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/juego/")
@Controller
public class JuegoController {
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("r")
    public String r(ModelMap m) {

        List<Juego> juegos = juegoService.findAll();
        m.put("juegos", juegos);
        m.put("view", "juego/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado
            m.put("view", "juego/c");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio
            // de sesión u otra página apropiada.
            return "redirect:/login"; // Redirige a la página de inicio de sesión
        }
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            juegoService.save(nombre);
            PRG.info("El juego con el nombre " + nombre + " ha sido creado", "/juego/c");
        } catch (Exception e) {
            PRG.error("El juego con el nombre " + nombre + " ya existe", "/juego/c");
        }
        return "redirect:/juego/r";
    }

    @GetMapping("u")
    public String update(@RequestParam("id") Long id, ModelMap m) {
        Juego juegos = juegoService.findById(id);
        m.put("juegos", juegos);

        m.put("view", "juegos/u");
        return "_t/frame";
    }

    @GetMapping("rDetailed")
    public String rDetailed(@RequestParam("id_elemento") Long id_elemento,
            ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Si el usuario está autenticado, continúa con la lógica para cargar la vista
            // rDetailed
            m.put("juego", juegoService.findByIdElemento(id_elemento));
            m.put("view", "juego/rDetailed");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
            return "redirect:/"; // Cambia "/login" por la ruta correcta de tu página de inicio de sesión
        }
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idJuego") Long idJuego,
            @RequestParam("nombre") String titulo,
            @RequestParam("clasificacion") String clasificacion,
            @RequestParam("duracion") Integer duracion,
            @RequestParam("estado") String estado,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaSalida") LocalDate fechaLanzamiento,
            @RequestParam("cuentaVotos") Integer cuentaVotos,
            @RequestParam("trailer") String trailer,
            @RequestParam("urlCompra") String url) throws DangerException {
        try {
            juegoService.update(idJuego, titulo, clasificacion, duracion, estado, plataforma, sinopsis,
                    fechaLanzamiento, cuentaVotos, trailer, url);
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizado", "/juego/r");
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/juego/r");
        }
        return "redirect:/juego/r";
    }

    // @PostMapping("rDetailed")
    // public String checklist(
    //         @RequestParam("idJuego") Long idJuego,
    //         HttpSession session,
    //         ModelMap m) throws DangerException {

    //     Usuario usuario = (Usuario) session.getAttribute("usuario");

    //     if (usuario == null) {
    //         return "redirect:/login";
    //     }

    //     Juego juego = juegoService.findByIdElemento(idJuego);
    //     System.out.println("JUUEGOI" + juego.getTitulo());
    //     List<Juego> juegosFav = usuario.getJuegosFav();

    //     if (!juegosFav.contains(juego)) {
    //         usuarioService.saveUsuarioJuegos(usuario, juego);
    //     }


    //     m.put("juego", juego);
    //     return "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento();
    // }

    @PostMapping("d")
    public String delete(
            @RequestParam("idJuego") Long idJuego) throws DangerException {
        try {
            juegoService.delete(idJuego);
        } catch (Exception e) {
            PRG.error("", "/pelicula/r");
        }
        return "redirect:/pelicula/r";
    }
}