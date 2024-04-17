package org.proyect.controller.web;

import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
import org.proyect.service.CategoriaService;
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

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("categorias", categoriaService.findAll());
        m.put("view", "categoria/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {

        m.put("view", "categoria/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception {
 
        try {
            categoriaService.save(nombre);
        } catch (Exception e) {
            PRG.error("El país " + nombre + " ya existe", "/categoria/c");
        }
        return "redirect:/categoria/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long id_Bean,
            ModelMap m) {
        m.put("_bean", categoriaService.findById(id_Bean));
        m.put("view", "categoria/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id_Bean") Long id_Bean,
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            categoriaService.update(id_Bean, nombre);
        } catch (Exception e) {
            PRG.error("El país no pudo ser actualizado", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long id_Bean) throws DangerException {
        try {
            categoriaService.delete(id_Bean);
        } catch (Exception e) {
            PRG.error("No se puede borrar un país que tenga algún nacido/residente", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }
}
