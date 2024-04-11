package org.proyect.controller.web;

import org.proyect.exception.DangerException;
import org.proyect.helper.PRG;
import org.proyect.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/actor")
@Controller
public class ActorController {

    @Autowired
    private ActorService actorService;
    
    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("actores", actorService.findAll());
        m.put("view", "actor/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {

        m.put("view", "actor/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception {
 
        try {
            actorService.save(nombre);
        } catch (Exception e) {
            PRG.error("El actor " + nombre + " ya existe", "/actor/c");
        }
        return "redirect:/actor/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long id_Actor,
            ModelMap m) {
        m.put("actor", actorService.findById(id_Actor));
        m.put("view", "actor/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id_Actor") Long id_Actor,
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            actorService.update(id_Actor, nombre);
        } catch (Exception e) {
            PRG.error("El actor no pudo ser actualizado", "/actor/r");
        }
        return "redirect:/actor/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long id_Actor) throws DangerException {
        try {
            actorService.delete(id_Actor);
        } catch (Exception e) {
            PRG.error("No se puede borrar un actor.", "/actor/r");
        }
        return "redirect:/actor/r";
    }
}

