package org.proyect.controller.web;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.exception.DangerException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.JuegoService;
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
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio de sesión u otra página apropiada.
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

    /*
     * @PostMapping("u")
     * public String updatePost(@RequestParam("idjuegos") Long
     * id, @RequestParam("dni") String dni,
     * 
     * @RequestParam("nombre") String nombre,
     * 
     * @RequestParam("pass") String pass,
     * 
     * @RequestParam("id-nace") Long idNace, @RequestParam("id-vive") Long idVive,
     * 
     * @RequestParam(value = "gustoId[]", required = false) List<Long> idsGusto,
     * 
     * @RequestParam(value = "odioId[]", required = false) List<Long> idsOdio)
     * throws DangerException, InfoException {
     * try {
     * juegoService.update(id, dni, nombre,pass, idNace, idVive, idsGusto,
     * idsOdio);
     * } catch (Exception e) {
     * PRG.error("La juegos con el nombre " + nombre + " ya existe",
     * "/juegos/u?id=" + id);
     * }
     * PRG.info("La juegos con el nombre  " + nombre + " ha sido actualizado",
     * "/juegos/r");
     * return "redirect:/juego/r";
     * }
     * 
     * @PostMapping("d")
     * public String delete(@RequestParam("id") Long idjuegos) throws
     * DangerException {
     * try {
     * juegoService.delete(idjuegos);
     * } catch (Exception e) {
     * PRG.error("Error al borrar a la juegos");
     * }
     * 
     * return "redirect:/juego/r";
     * }
     */
}
