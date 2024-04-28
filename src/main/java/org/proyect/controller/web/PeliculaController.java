package org.proyect.controller.web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.domain.Pelicula;
import org.proyect.exception.DangerException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.service.CategoriaService;
import org.proyect.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
@RequestMapping("/pelicula/")
@Controller
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private CategoriaService categoriaServiceService;


    @GetMapping("r")
    public String r(ModelMap m) {

        List<Pelicula> peliculas = peliculaService.findAll();
        
        m.put("peliculas", peliculas);
        m.put("view", "/pelicula/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) { // Verifica si el usuario está autenticado
            List<Categoria> categorias = categoriaServiceService.findAll();
            m.put("categorias", categorias);
            m.put("view", "pelicula/c");
            
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio de sesión u otra página apropiada.
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }
    }

    @PostMapping("c")
public String cPost(
        @RequestParam("titulo") String titulo,
        @RequestParam(value = "categoria[]", required = false) List<Long> categoria,
        @RequestParam("clasificacion") String clasificacion,
        @RequestParam("duracion") Integer duracion,
        @RequestParam("estado") String estado,
        @RequestParam("plataforma") String plataforma,
        @RequestParam("sinopsis") String sinopsis,
        @RequestParam("fechaLanzamiento") LocalDate fechaLanzamiento,
        @RequestParam("imagen") MultipartFile imagen,
        @RequestParam("trailer") String trailer,
        @RequestParam("url") String url) throws DangerException {
    try {
        String nombreImagen = null; // variable para guardar el nombre de la imagen
        if (!imagen.isEmpty()) {
            String directorioImagenes = "src//main//resources//static/img/peliculas";
            Path rutaDirectorio = Paths.get(directorioImagenes);
            
            // Verifica si el directorio existe, si no, intenta crearlo
            if (!Files.exists(rutaDirectorio)) {
                Files.createDirectories(rutaDirectorio);
            }
            
            byte[] bytesImg = imagen.getBytes();
            nombreImagen = imagen.getOriginalFilename(); // guardamos el nombre de la imagen
            
            Path rutaCompleta = rutaDirectorio.resolve(nombreImagen);
            
            try (OutputStream os = Files.newOutputStream(rutaCompleta)) {
                os.write(bytesImg);
            } catch (IOException e) {
                // Manejo de errores al escribir el archivo
                throw new RuntimeException("Error al escribir la imagen", e);
            }
        }
        peliculaService.save(titulo, categoria, clasificacion, duracion, estado, plataforma, sinopsis, fechaLanzamiento, nombreImagen, trailer, url);
        PRG.info("La película con nombre '" + titulo + "' ha sido creada", "/pelicula/c");
    } catch (Exception e) {
        PRG.error("Error al crear la película: " + e.getMessage(), "/pelicula/c");
    }
    return "redirect:/pelicula/r";
}



    @GetMapping("u")
    public String update(@RequestParam("id") Long id, ModelMap m) {
        Pelicula pelicula = peliculaService.findById(id);
        m.put("pelicula", pelicula);

        m.put("view", "pelicula/u");
        return "_t/frame";
    }

    /*
     * @PostMapping("u")
     * public String updatePost(@RequestParam("idpelicula") Long
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
     * peliculaService.update(id, dni, nombre,pass, idNace, idVive, idsGusto,
     * idsOdio);
     * } catch (Exception e) {
     * PRG.error("La pelicula con el nombre " + nombre + " ya existe",
     * "/pelicula/u?id=" + id);
     * }
     * PRG.info("La pelicula con el nombre  " + nombre + " ha sido actualizado",
     * "/pelicula/r");
     * return "redirect:/pelicula/r";
     * }
     * 
     * @PostMapping("d")
     * public String delete(@RequestParam("id") Long idpelicula) throws
     * DangerException {
     * try {
     * peliculaService.delete(idpelicula);
     * } catch (Exception e) {
     * PRG.error("Error al borrar a la pelicula");
     * }
     * 
     * return "redirect:/pelicula/r";
     * }
     */
}
