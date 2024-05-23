package org.proyect.controller.web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.proyect.domain.Categoria;
import org.proyect.domain.Juego;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.H;
import org.proyect.helper.JuegoValidator;
import org.proyect.helper.PRG;
import org.proyect.service.CategoriaService;
import org.proyect.service.JuegoService;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/juego/")
@Controller
public class JuegoController {
    @Autowired
    private JuegoService juegoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioService usuarioService;

    // @GetMapping("r")
    // public String r(ModelMap m) {

    // List<Juego> juegos = juegoService.findAll();
    // m.put("juegos", juegos);
    // m.put("view", "juego/r");
    // return "_t/frame";
    // }
    @GetMapping("r")
    public String r(@RequestParam(defaultValue = "0") int page, ModelMap m) {
        Pageable pageable = PageRequest.of(page, 12); // 10 películas por página
        Page<Juego> juegosPage = juegoService.findAll(pageable);
        List<Categoria> categorias = categoriaService.findAll();
        List<String> clasificaciones = Arrays.asList("G", "PG", "R13", "R15", "M", "R16", "RP16");
        List<Juego> juegos = juegosPage.getContent();
        m.put("juegos", juegos);
        m.put("currentPage", page);
        m.put("categorias", categorias);
        m.put("clasificaciones", clasificaciones);
        m.put("totalPages", juegosPage.getTotalPages());
        m.put("view", "/juego/r");
        return "_t/frame";
    }

    @GetMapping("rAdmin")
    public String rAdmin(
            ModelMap m, HttpSession s) {
        if (H.isRolOk("admin", s)) { // Verifica si el usuario está autenticado
            m.put("juegos", juegoService.findAll());
            m.put("view", "juego/rAdmin");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado, puedes redirigirlo a una página de inicio
            // de sesión u otra página apropiada.
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado
            m.put("view", "juego/c");
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
            @RequestParam("puntuacion") Integer puntuacion,
            @RequestParam("estado") String estado,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaLanzamiento") LocalDate fechaLanzamiento,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("trailer") String trailer,
            @RequestParam("url") String url) throws DangerException {
                Boolean creado = false;
        try {
            // VALIDALOR DE DATOS

            if (JuegoValidator.ValidarDatosC(titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                    categoria, sinopsis, fechaLanzamiento, puntuacion, trailer, url, imagen)) {
                String nombreImagen = null; // variable para guardar el nombre de la imagen
                if (!imagen.isEmpty()) {
                    String directorioImagenes = "src//main//resources//static/img/juegos";
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

                juegoService.save(titulo, categoria, clasificacion, duracion, puntuacion, estado, plataforma,
                        sinopsis,
                        fechaLanzamiento, nombreImagen, trailer, url);
                PRG.info("La película con nombre '" + titulo + "' ha sido creada", "/juego/c");
            }

        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/juego/c");
        }
        if (creado) {
            PRG.info("El juego con el nombre " + nombre + " ha sido creado", "/juego/c");
        }
        if (creado) {
            PRG.info("El juego con el nombre " + nombre + " ha sido creado", "/juego/c");
        } catch (Exception e) {
            PRG.error("El juego con el nombre " + nombre + " ya existe", "/juego/c");
        }
        return "redirect:/juego/r";
    }

     @PostMapping("u")
    public String updatePost(
            @RequestParam("idjuego") Long idjuego,
            @RequestParam("nombre") String titulo,
            @RequestParam("clasificacion") String clasificacion,
            @RequestParam("duracion") Integer duracion,
            @RequestParam("estado") String estado,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("puntuacion") Integer puntuacion,
            @RequestParam(value = "categoriaId[]", required = false) List<Long> idsCategoria,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaSalida") LocalDate fechaLanzamiento,
            @RequestParam("cuentaVotos") Integer cuentaVotos,
            @RequestParam("trailer") String trailer,
            @RequestParam("urlCompra") String url) throws DangerException {

        try {
            if (JuegoValidator.ValidarDatos(titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                    idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url)) {
                juegoService.update(idjuego, titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                        idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url);
            }
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizada", "/juego/r");
        }catch (Exception e) {
            PRG.error("El juego con el nombre " + nombre + " ya existe", "/juego/c");
        }
        if (creado) {
            PRG.info("El juego con el nombre " + nombre + " ha sido creado", "/juego/c");
        }
        return "redirect:/juego/r";
    }

    @GetMapping("rDetailed")
    public String rDetailed(@RequestParam("id_elemento") Long id_elemento,
            ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Si el usuario está autenticado, continúa con la lógica para cargar la vista
            // rDetailed
            m.put("juego", juegoService.findByIdElemento(id_elemento));
            m.put("view", "juego/rDetailed");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
            return "redirect:/"; // Cambia "/login" por la ruta correcta de tu página de inicio de sesión
        }
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idJuego") Long idJuego,
            @RequestParam("nombre") String titulo,
            @RequestParam("clasificacion") String clasificacion,
            @RequestParam("duracion") Integer duracion,
            @RequestParam("estado") String estado,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaSalida") LocalDate fechaLanzamiento,
            @RequestParam("cuentaVotos") Integer cuentaVotos,
            @RequestParam("trailer") String trailer,
            @RequestParam("urlCompra") String url) throws DangerException,InfoException {
                Boolean creado = false;

        try {
            juegoService.update(idJuego, titulo, clasificacion, duracion, estado, plataforma, sinopsis,
                    fechaLanzamiento, cuentaVotos, trailer, url);
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/juego/r");
        }
        if (creado) {
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizado", "/juego/r");
        }
        return "redirect:/juego/r";
    }

    @PostMapping("rDetailed")
    public String checklist(
            @RequestParam("idJuego") Long idJuego,
            HttpSession session,
            ModelMap m) throws DangerException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/";
        }

        Juego juego = juegoService.findByIdElemento(idJuego);
        List<Juego> juegosFav = usuario.getJuegosFav();

        if (!juegosFav.contains(juego)) {
            usuarioService.saveUsuarioJuegos(usuario, juego);
        }

        m.put("juego", juego);

        return "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento();
    }

    @PostMapping("rDetailedRating")
    public String puntuacion(
            @RequestParam("idJuego") Long idJuego,
            @RequestParam("rating") Long puntos,
            HttpSession session,
            ModelMap m) throws DangerException {

        Juego juego = juegoService.findByIdElemento(idJuego);

        m.put("juego", juego);
        m.put("calificacion", juegoService.setCalificacion(juego, puntos));
        return "redirect:/juego/rDetailed?id_elemento=" + juego.getIdElemento();
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("idJuego") Long idJuego) throws DangerException {
        try {
            juegoService.delete(idJuego);
        } catch (Exception e) {
            PRG.error("", "/juego/r");
        }
        return "redirect:/juego/r";
    }

    @GetMapping("filtrar")
    public String filtrar(@RequestParam(name = "idCategoria", required = false) Long idCategoria,
            @RequestParam(name = "clasificacion", required = false) String clasificacion,
            ModelMap m) {
        List<Juego> juegosFiltradas;
        List<Juego> juegos = juegoService.findAll();
        List<String> clasificaciones = Arrays.asList("PEGI 3", "PEGI 7", "PEGI 12", "PEGI 16", "PEGI 18");
        Categoria categoria = null;

        if (idCategoria != null) {
            categoria = categoriaService.findById(idCategoria);
        }

        if (idCategoria != null) {
            juegosFiltradas = new ArrayList<>();
            for (Juego juego : juegos) {
                for (Categoria cat : juego.getCategorias()) {
                    if (cat.getIdCategoria().equals(idCategoria)) {
                        juegosFiltradas.add(juego);
                        break;
                    }
                }
            }
        } else if (clasificacion != null) {
            juegosFiltradas = new ArrayList<>();
            for (Juego juego : juegos) {
                if (juego.getClasificacion().equals(clasificacion)) {
                    juegosFiltradas.add(juego);
                }
            }
        } else {
            juegosFiltradas = juegos;
        }

        m.put("juegos", juegosFiltradas);
        m.put("categorias", categoriaService.findAll());
        m.put("clasificaciones", clasificaciones);
        m.put("categoria", categoria);
        m.put("view", "juego/r");
        return "_t/frame";
    }
}