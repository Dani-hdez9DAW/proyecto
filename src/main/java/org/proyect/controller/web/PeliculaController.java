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
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/pelicula/")
@Controller
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private CategoriaService categoriaServiceService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("r")
    public String r(ModelMap m) {

        List<Pelicula> peliculas = peliculaService.findAll();

        m.put("peliculas", peliculas);
        m.put("view", "/pelicula/r");
        return "_t/frame";
    }

    @GetMapping("rAdmin")
    public String rAdmin(
            ModelMap m, HttpSession s) {
        if (H.isRolOk("admin", s)) { // Verifica si el usuario está autenticado
            m.put("peliculas", peliculaService.findAll());
            m.put("view", "pelicula/rAdmin");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio
            // de sesión u otra página apropiada.
            return "/"; // Redirige a la página de inicio de sesión
        }
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) { // Verifica si el usuario está autenticado
            List<Categoria> categorias = categoriaServiceService.findAll();
            m.put("categorias", categorias);
            m.put("view", "pelicula/c");

            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio
            // de sesión u otra página apropiada.
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
            peliculaService.save(titulo, categoria, clasificacion, duracion, estado, plataforma, sinopsis,
                    fechaLanzamiento, nombreImagen, trailer, url);
            PRG.info("La película con nombre '" + titulo + "' ha sido creada", "/pelicula/c");
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/pelicula/c");
        }
        return "redirect:/pelicula/r";
    }

   @GetMapping("rDetailed")
public String rDetailed(@RequestParam("id_elemento") Long id_elemento,
                        ModelMap m, HttpSession session) {
    if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
        // Si el usuario está autenticado, continúa con la lógica para cargar la vista rDetailed
        m.put("pelicula", peliculaService.findByIdElemento(id_elemento));
        m.put("view", "pelicula/rDetailed");
        return "_t/frame";
    } else {
        // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la página de inicio de sesión
        return "redirect:/"; // Cambia "/login" por la ruta correcta de tu página de inicio de sesión
    }
}

    @PostMapping("u")
    public String updatePost(
        @RequestParam("idpelicula") Long idPelicula,
        @RequestParam("nombre") String titulo,
        @RequestParam("clasificacion") String clasificacion,
        @RequestParam("duracion") Integer duracion,
        @RequestParam("estado") String estado,
        @RequestParam("plataforma") String plataforma,
        @RequestParam("sinopsis") String sinopsis,
        @RequestParam("fechaSalida") LocalDate fechaLanzamiento,
        @RequestParam("cuentaVotos") Integer cuentaVotos,
        @RequestParam("trailer") String trailer,
        @RequestParam("urlCompra") String url) throws DangerException {
        try {
            peliculaService.update(idPelicula,titulo, clasificacion, duracion, estado, plataforma, sinopsis,
                    fechaLanzamiento,cuentaVotos, trailer, url);
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizado", "/pelicula/r");
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/pelicula/r");
        }
        return "redirect:/pelicula/r";
    }
    
    @PostMapping("checklist")
    public void checklist(
        @RequestParam("idpelicula") Long idpelicula,
        @RequestParam("idusuario") Long idusuario) throws DangerException {
            Usuario usuario =  usuarioService.findById(idusuario);
            Pelicula pelicula = peliculaService.findByIdElemento(idpelicula);

            System.out.println(idpelicula);
            System.out.println(idusuario);
            List<Pelicula> peliculasUsuario = usuario.getPeliculasFav();
            peliculasUsuario.add(pelicula);
    }


    @PostMapping("d")
    public String delete(
            @RequestParam("idpelicula") Long idPelicula) throws DangerException {
        try {
            peliculaService.delete(idPelicula);
        } catch (Exception e) {
            PRG.error("", "/pelicula/r");
        }
        return "redirect:/pelicula/r";
    }
}
    

