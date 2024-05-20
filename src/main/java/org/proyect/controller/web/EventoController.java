package org.proyect.controller.web;

import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.PRG;
import org.proyect.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
@RequestMapping("/evento/")
@Controller
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("eventos", eventoService.findAll());
        m.put("view", "evento/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {
        m.put("view", "evento/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception, DangerException, InfoException {
                Boolean creado = false;
        try {
            eventoService.save(nombre);
        } catch (Exception e) {
            PRG.error("El país " + nombre + " ya existe", "/categoria/c");
        }
        if (creado) {
            PRG.info("El país " + nombre + " se ha creado", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long id_Bean,
            ModelMap m) {
        m.put("_bean", eventoService.findById(id_Bean));
        m.put("view", "categoria/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id_Bean") Long id_Bean,
            @RequestParam("nombre") String nombre) throws DangerException, InfoException {
                Boolean creado = false;
        try {
            eventoService.update(id_Bean, nombre);
        } catch (Exception e) {
            PRG.error("El país no pudo ser actualizado", "/categoria/r");
        }
        if (creado) {
            PRG.info("El país se ha actualizado", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long id_Bean) throws DangerException, InfoException {
                Boolean creado = false;

        try {
            eventoService.delete(id_Bean);
        } catch (Exception e) {
            PRG.error("No se puede borrar un país que tenga algún nacido/residente", "/categoria/r");
        }
        if (creado) {
            PRG.info("Se ha borrado", "/categoria/r");
        }
        return "redirect:/categoria/r";
    }
}
