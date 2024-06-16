package org.proyect.controller.web;

import java.time.LocalDate;
import java.util.List;

import org.proyect.domain.Evento;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.ComentarioValidator;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.EventoService;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EventoService eventoService;

    @GetMapping("/r")
    public String listarEventos(ModelMap model) {
        List<Evento> eventos = eventoService.findAll();
        model.put("eventos", eventos);
        model.put("view", "evento/r");
        return "_t/frame";
    }

    @GetMapping("/rAdmin")
    public String listarEventosAdmin(ModelMap model, HttpSession session) {
        if (H.isRolOk("admin", session)) {
            model.put("eventos", eventoService.findAll());
            model.put("view", "evento/rAdmin");
            return "_t/frame";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/c")
    public String crearEvento(ModelMap model, HttpSession session) {
        if (H.isRolOk("admin", session)) {
            model.put("tipos", eventoService.findAllTipos());
            model.put("view", "evento/c");
            return "_t/frame";
        }
        return "redirect:/";
    }

    @PostMapping("/c")
    public String crearEventoPost(Evento nuevoEvento) throws DangerException {
        try {
            eventoService.save(nuevoEvento);
        } catch (Exception e) {
            PRG.error("Error al crear el evento", "evento/c");
        }

        return "redirect:/evento/c";
    }

    @GetMapping("/u")
    public String actualizarEvento(@RequestParam("id") Long idEvento, ModelMap model, HttpSession session) {
        if (H.isRolOk("admin", session)) {
            Evento evento = eventoService.findById(idEvento);
            model.put("evento", evento);
            model.put("tipos", eventoService.findAllTipos());
            model.put("view", "evento/u");
            return "_t/frame";
        }
        return "redirect:/";
    }

    @PostMapping("/u")
    public String actualizarEventoPost(@RequestParam("id") Long idEvento,
            @RequestParam("nombre") String nombre,
            @RequestParam("localizacion") String localizacion,
            @RequestParam("tipo") String tipo,
            @RequestParam("fechaEvento") LocalDate fechaEvento, HttpSession session) throws DangerException {
        if (!ComentarioValidator.validarComentario(nombre)) {
            PRG.error("El nombre del evento esta vacio o tiene palabras prohibidas", "/evento/u?id=" + idEvento);
        }
        if (!ComentarioValidator.validarComentario(localizacion)) {
            PRG.error("La localizacion del evento esta vacio o tiene palabras prohibidas", "/evento/u?id=" + idEvento);
        }
        eventoService.update(idEvento, nombre, localizacion, tipo, fechaEvento);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String correoUsuario = usuario.getCorreo();
        usuarioService.modificacionPuntos(correoUsuario, 4);
        return "redirect:/evento/rAdmin";
    }

    @PostMapping("/d")
    public String eliminarEvento(@RequestParam("idevento") Long idEvento) throws DangerException {
        try {
            eventoService.delete(idEvento);
        } catch (Exception e) {
            PRG.error("Error al borrar el evento", "/evento/rAdmin");
        }

        return "redirect:/evento/rAdmin";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Evento> getAllEventos() {
        return eventoService.findAll();
    }
}