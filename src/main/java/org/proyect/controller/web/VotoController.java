package org.proyect.controller.web;

import java.util.List;

import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.domain.Voto;
import org.proyect.service.PeliculaService;
import org.proyect.service.UsuarioService;
import org.proyect.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/voto")
@Controller
public class VotoController {

    @Autowired
    private VotoService votoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PeliculaService peliculaService;

    @PostMapping("/votar")
    public void votarPelicula(@RequestParam Long usuarioId, @RequestParam Long peliculaId,
            @RequestParam Long puntaje) {
        // Obtener el usuario y la pelicula por sus IDs
        Usuario usuario = usuarioService.findById(usuarioId);
        Pelicula pelicula = peliculaService.findByIdElemento(peliculaId);

        // Llamar al servicio para votar
        votoService.votarPelicula(usuario, pelicula, puntaje);
    }

    @GetMapping("/r")
    public String obtenerMisVotos(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Voto> misVotos = votoService.findAllByUsuarioAndJuegoIsNull(usuario); // Filtrar solo películas

        m.addAttribute("votos", misVotos);
        m.put("view", "usuario/misVotos");
        return "_t/frame";
    }

    @GetMapping("/rJuego")
    public String obtenerMisVotosJuego(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Voto> misVotos = votoService.findAllByUsuarioAndPeliculaIsNull(usuario); // Filtrar solo juegos

        m.addAttribute("votos", misVotos);
        m.put("view", "usuario/misVotosJuegos");
        return "_t/frame";
    }

}
