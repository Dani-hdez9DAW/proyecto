package org.proyect.controller.web;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
import org.proyect.repository.JuegoRepository;
import org.proyect.repository.PeliculaRepository;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("c")
    public String c(ModelMap m) {
        m.put("view", "usuario/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("pass") String password) throws DangerException {
        try {
            usuarioService.save(nombre, password);
            PRG.info("El usuario  con el nombre " + nombre + " ha sido creado", "/");
        } catch (Exception e) {
            PRG.error("El usuario  con el nombre " + nombre + " ya existe", "/");
        }
        return "redirect:/";
    }
}
