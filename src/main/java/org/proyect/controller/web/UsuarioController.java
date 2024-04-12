package org.proyect.controller.web;

import java.util.List;

import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
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

    @GetMapping("r")
    public String r(ModelMap m) {

        List<Usuario> usuarios = usuarioService.findAll();
        m.put("usuarios", usuarios);
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
            PRG.info("El usuario  con el nombre " + nombre + " ha sido creado", "/usuario/c");
        } catch (Exception e) {
            PRG.error("El usuario  con el nombre " + nombre + " ya existe", "/usuario/c");
        }
        return "redirect:/usuario/r";
    }
}
