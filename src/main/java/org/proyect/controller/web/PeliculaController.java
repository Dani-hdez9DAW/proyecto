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
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.helper.PeliculaValidator;
import org.proyect.service.CategoriaService;
import org.proyect.service.PeliculaService;
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

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/pelicula/")
@Controller
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioService usuarioService;

    // @GetMapping("r")
    // public String r(ModelMap m) {

    // List<Pelicula> peliculas = peliculaService.findAll();
    // List<Categoria> categorias = categoriaService.findAll();
    // List<String> clasificaciones = Arrays.asList("G", "PG", "R13", "R15", "M",
    // "R16", "RP16");

    // m.put("peliculas", peliculas);
    // m.put("categorias",categorias);
    // m.put("clasificaciones",clasificaciones);
    // m.put("view", "/pelicula/r");
    // return "_t/frame";
    // }
    @GetMapping("r")
    public String r(@RequestParam(defaultValue = "0") int page, ModelMap m) {
        Pageable pageable = PageRequest.of(page, 12); // 10 películas por página
        Page<Pelicula> peliculasPage = peliculaService.findAll(pageable);
        List<Categoria> categorias = categoriaService.findAll();
        List<String> clasificaciones = Arrays.asList("R", "PG-13", "PG-16", "PG-17");
        List<Pelicula> peliculas = peliculasPage.getContent();
        m.put("peliculas", peliculas);
        m.put("currentPage", page);
        m.put("categorias", categorias);
        m.put("clasificaciones", clasificaciones);
        m.put("totalPages", peliculasPage.getTotalPages());
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
            return "redirect:/"; // Redirige a la página de inicio de sesión
        }
    }

    @GetMapping("c")
    public String c(ModelMap m, HttpSession session) {
        if (H.isRolOk("admin", session)) { // Verifica si el usuario está autenticado
            List<Categoria> categorias = categoriaService.findAll();
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
            @RequestParam("puntuacion") Integer puntuacion,
            @RequestParam("estado") String estado,
            @RequestParam("plataforma") String plataforma,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaLanzamiento") LocalDate fechaLanzamiento,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("trailer") String trailer,
            @RequestParam("url") String url) throws DangerException, InfoException {
                Boolean creado = false;
        try {
            // VALIDALOR DE DATOS

            if (PeliculaValidator.ValidarDatosC(titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                    categoria, sinopsis, fechaLanzamiento, puntuacion, trailer, url, imagen)) {
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
            peliculaService.save(titulo, categoria, clasificacion, duracion, puntuacion, estado, plataforma, sinopsis,
                    fechaLanzamiento, nombreImagen, trailer, url);
        }
            
            // PRG.info("La película con nombre '" + titulo + "' ha sido creada", "/pelicula/c");
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "/pelicula/c");
        }
        if (creado) {
            PRG.info("La película con nombre '" + titulo + "' ha sido creada", "/pelicula/c");
        }
        return "redirect:/pelicula/r";
    }

    @GetMapping("rDetailed")
    public String rDetailed(@RequestParam("id_elemento") Long id_elemento,
            ModelMap m, HttpSession session) {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Si el usuario está autenticado, continúa con la lógica para cargar la vista
            // rDetailed
            m.put("categorias", categoriaService.findAll());
            m.put("categoriasPertenecientes", peliculaService.findByIdElemento(id_elemento).getCategorias());
            m.put("pelicula", peliculaService.findByIdElemento(id_elemento));
            m.put("view", "pelicula/rDetailed");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
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
            @RequestParam("puntuacion") Integer puntuacion,
            @RequestParam(value = "categoriaId[]", required = false) List<Long> idsCategoria,
            @RequestParam("sinopsis") String sinopsis,
            @RequestParam("fechaSalida") LocalDate fechaLanzamiento,
            @RequestParam("cuentaVotos") Integer cuentaVotos,
            @RequestParam("trailer") String trailer,
            @RequestParam("urlCompra") String url) throws DangerException,InfoException {
                Boolean creado = false;

        try {
            if (PeliculaValidator.ValidarDatos(titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                    idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url)) {
                peliculaService.update(idPelicula, titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                        idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url);
            }
        } catch (Exception e) {
            PRG.error("Error al actualizar la película: " + e.getMessage(), "/pelicula/r");
        }
        if (creado) {
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizado", "/pelicula/r");
        }
        if (creado) {
            PRG.info("La película con nombre '" + titulo + "' ha sido actualizado", "/pelicula/r");
        }
        return "redirect:/pelicula/r";
    }

    @PostMapping("rDetailed")
    public String checklist(
            @RequestParam("idPelicula") Long idPelicula,
            HttpSession session,
            ModelMap m) throws DangerException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }
        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);
        List<Pelicula> peliculasFav = usuario.getPeliculasFav();
        if (!peliculasFav.contains(pelicula)) {
            usuarioService.saveUsuarioPeliculas(usuario, pelicula);
        }

        // System.out.println("ID de la película: " + pelicula.getIdElemento());
        // System.out.println("Título de la película: " + pelicula.getTitulo());

        m.put("pelicula", pelicula);

        return "redirect:/pelicula/rDetailed?id_elemento=" + pelicula.getIdElemento();
    }

    @PostMapping("rDetailedRating")
    public String puntuacion(
            @RequestParam("idPelicula") Long idPelicula,
            @RequestParam("rating") Long puntos,
            HttpSession session,
            ModelMap m) throws DangerException {

        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);

        m.put("pelicula", pelicula);
        m.put("calificacion", peliculaService.setCalificacion(pelicula, puntos));
        return "redirect:/pelicula/rDetailed?id_elemento=" + pelicula.getIdElemento();
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

    // @GetMapping("filtrarPorCategoria")
    // public String filtrarPorCategoria(@RequestParam(name = "idCategoria",
    // required = false) Long idCategoria,
    // ModelMap m) {
    // List<Pelicula> peliculasFiltradas;
    // List<Pelicula> peliculas = peliculaService.findAll();
    // Categoria categoria = null;

    // if (idCategoria != null) {
    // categoria = categoriaService.findById(idCategoria);
    // }

    // if (categoria != null) {
    // peliculasFiltradas = new ArrayList<>();
    // for (Pelicula pelicula : peliculas) {
    // for (Categoria cat : pelicula.getCategorias()) {
    // if (cat.getIdCategoria().equals(categoria.getIdCategoria())) {
    // peliculasFiltradas.add(pelicula);
    // break;
    // }
    // }
    // }
    // } else {
    // peliculasFiltradas = peliculas;
    // }

    // m.put("peliculas", peliculasFiltradas);
    // m.put("categorias", categoriaService.findAll());
    // m.put("categoria", categoria);
    // m.put("view", "pelicula/r");
    // return "_t/frame";
    // }

    // @GetMapping("filtrarPorClasificacion")
    // public String filtrarPorClasificacion(@RequestParam(name = "clasificacion",
    // required = false) String clasificacion,
    // ModelMap m) {
    // List<Pelicula> peliculasFiltradas = new ArrayList<Pelicula>();
    // List<Pelicula> peliculas = peliculaService.findAll();
    // List<String> clasificaciones = Arrays.asList("G", "PG", "R13", "R15", "M",
    // "R16", "RP16");

    // for(Pelicula pelicula : peliculas){
    // if(pelicula.getClasificacion() == clasificacion){
    // peliculasFiltradas.add(pelicula);
    // }
    // }
    // if(clasificacion==null){
    // peliculasFiltradas = peliculas;
    // }

    // m.put("peliculas", peliculasFiltradas);
    // m.put("categorias", categoriaService.findAll());
    // m.put("clasificaciones",clasificaciones);
    // m.put("view", "pelicula/r");
    // return "_t/frame";
    // }

    @GetMapping("filtrar")
    public String filtrar(@RequestParam(name = "idCategoria", required = false) Long idCategoria,
            @RequestParam(name = "clasificacion", required = false) String clasificacion,
            ModelMap m) {
        List<Pelicula> peliculasFiltradas;
        List<Pelicula> peliculas = peliculaService.findAll();
        List<String> clasificaciones = Arrays.asList("G", "PG", "R13", "R15", "M", "R16", "RP16");
        Categoria categoria = null;

        if (idCategoria != null) {
            categoria = categoriaService.findById(idCategoria);
        }

        if (idCategoria != null) {
            peliculasFiltradas = new ArrayList<>();
            for (Pelicula pelicula : peliculas) {
                for (Categoria cat : pelicula.getCategorias()) {
                    if (cat.getIdCategoria().equals(idCategoria)) {
                        peliculasFiltradas.add(pelicula);
                        break;
                    }
                }
            }
        } else if (clasificacion != null) {
            peliculasFiltradas = new ArrayList<>();
            for (Pelicula pelicula : peliculas) {
                if (pelicula.getClasificacion().equals(clasificacion)) {
                    peliculasFiltradas.add(pelicula);
                }
            }
        } else {
            peliculasFiltradas = peliculas;
        }

        m.put("peliculas", peliculasFiltradas);
        m.put("categorias", categoriaService.findAll());
        m.put("clasificaciones", clasificaciones);
        m.put("categoria", categoria);
        m.put("view", "pelicula/r");
        return "_t/frame";
    }

    // CARROUSELL
    // @GetMapping("/peliculas")
    // public String mostrarPeliculas(Model model) {
    // List<Pelicula> peliculas = peliculaService.obtenerTodasLasPeliculas();
    // model.addAttribute("peliculas", peliculas);
    // return "nombre-de-tu-vista"; // Reemplaza con el nombre de tu archivo HTML
    // }
}