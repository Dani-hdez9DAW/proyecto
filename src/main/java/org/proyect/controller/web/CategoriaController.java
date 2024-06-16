package org.proyect.controller.web;

import java.util.Arrays;
import java.util.List;

import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.CategoriaValidator;
import org.proyect.exception.InfoException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.CategoriaService;
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

@RequestMapping("/categoria/")
@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        List<String> classArray = Arrays.asList(
                "one", "two", "three", "five", "six", "seven", "eight", "nine",
                "ten", "eleven", "tlv", "thirteen", "ftn", "fith", "sith", "sevth");

        m.addAttribute("classArray", classArray);
        m.put("categorias", categoriaService.findAll());
        m.put("view", "categoria/r");
        return "_t/frame";
    }

    @GetMapping("rAdmin")
    public String rAdmin(
            ModelMap m, HttpSession s) {
        if (H.isRolOk("admin", s)) { // Verifica si el usuario está autenticado
            m.put("peliculas", peliculaService.findAll());
            m.put("categorias", categoriaService.findAll());
            m.put("view", "categoria/rAdmin");
            return "_t/frame";
        } else {
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {
        if (H.isRolOk("admin", s)) { // Verifica si el usuario está autenticado
            m.put("view", "categoria/c");
            return "_t/frame";
        } else {
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception, DangerException, InfoException {
        Boolean creado = false;
        try {
            if (CategoriaValidator.validarCategoria(nombre)) {
                categoriaService.save(nombre);
                // GESTION DE PUNTOS DEL USUARIO
                Usuario usuario = (Usuario) s.getAttribute("usuario");
                String correoUsuario = usuario.getCorreo();
                usuarioService.modificacionPuntos(correoUsuario, 6);
            }

        } catch (Exception e) {
            // PRG.error("La categoria " + nombre + " ya existe", "/categoria/c");
        }
        if (creado) {
            // PRG.info("La categoria " + nombre + " se ha creado", "/categoria/rAdmin");
        }
        return "redirect:/categoria/rAdmin";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idCategoria,
            ModelMap m, HttpSession s) {
        if (H.isRolOk("admin", s)) { // Verifica si el usuario está autenticado
            m.put("view", "categoria/c");
            m.put("categoria", categoriaService.findById(idCategoria));
            m.put("view", "categoria/u");
            return "_t/frame";
        }
        return "redirect:/"; // Redirige a la página de inicio de sesión

    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idcategoria") Long idCategoria,
            @RequestParam("nombre") String nombre, HttpSession session) throws DangerException, InfoException {
        Boolean creado = false;

        try {
            categoriaService.update(idCategoria, nombre);
            // GESTION DE PUNTOS DEL USUARIO
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            String correoUsuario = usuario.getCorreo();
            usuarioService.modificacionPuntos(correoUsuario, 3);
        } catch (Exception e) {
            // PRG.error("La categoria no pudo ser actualizada", "/categoria/r");
        }
        if (creado) {
            // PRG.info("La categoria se ha actualizado", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long id_Bean) throws DangerException, InfoException {
        Boolean creado = false;

        try {
            categoriaService.delete(id_Bean);
        } catch (Exception e) {
            // PRG.error("Se ha eliminado la categoria", "/categoria/r");
        }
        if (creado) {
            // PRG.info("Se ha eliminado la categoria", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }
}
