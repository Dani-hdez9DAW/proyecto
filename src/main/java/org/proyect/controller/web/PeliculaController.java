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
import org.proyect.domain.Voto;
import org.proyect.exception.DangerException;
import org.proyect.exception.InfoException;
import org.proyect.helper.ComentarioValidator;
import org.proyect.helper.H;
import org.proyect.helper.PRG;
import org.proyect.helper.PeliculaValidator;
import org.proyect.service.CategoriaService;
import org.proyect.service.ComentarioService;
import org.proyect.service.PeliculaService;
import org.proyect.service.UsuarioService;
import org.proyect.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

@RequestMapping("/pelicula/")
@Controller
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    private VotoService votoService;

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
        m.put("view", "pelicula/r");
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
            @RequestParam("url") String url, HttpSession session) throws DangerException, InfoException {
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
                peliculaService.save(titulo, categoria, clasificacion, duracion, puntuacion, estado, plataforma,
                        sinopsis,
                        fechaLanzamiento, nombreImagen, trailer, url);
                // GESTION DE PUNTOS DEL USUARIO
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                String correoUsuario = usuario.getCorreo();
                usuarioService.modificacionPuntos(correoUsuario, 15);
            } // PRG.info("La película con nombre '" + titulo + "' ha sido creada",
              // "/pelicula/c");
        } catch (Exception e) {
            PRG.error("Error al crear la película: " + e.getMessage(), "pelicula/c");
        }
        if (creado) {
            PRG.info("La película con nombre '" + titulo + "' ha sido creada", "pelicula/c");
        }
        return "redirect:/pelicula/r";
    }

    @GetMapping("rDetailed")
    public String rDetailed(@RequestParam("id_elemento") Long id_elemento,
            ModelMap m, HttpSession session) throws DangerException {
        if (H.isRolOk("auth", session)) { // Verifica si el usuario está autenticado y tiene el rol "auth"
            // Obtenemos la película y el usuario actual
            Pelicula pelicula = peliculaService.findByIdElemento(id_elemento);
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Verificamos si el usuario ha votado esta película
            Voto votoUsuario = votoService.findByUsuarioAndPelicula(usuario, pelicula);
            Long puntajeUsuario = (votoUsuario != null) ? votoUsuario.getPuntaje() : null;

            boolean esFavorita = false;
            for (Pelicula favPelicula : usuario.getPeliculasFav()) {
                if (favPelicula.getIdElemento().equals(pelicula.getIdElemento())) {
                    esFavorita = true;
                    break;
                }
            }

            // Pasamos los datos necesarios al modelo
            m.put("pelicula", pelicula);
            m.put("categorias", categoriaService.findAll());
            m.put("comentarios", comentarioService.findByPeliculaComentarios(id_elemento));
            m.put("categoriasPertenecientes", pelicula.getCategorias());
            m.put("puntajeUsuario", puntajeUsuario); // Enviamos el puntaje del usuario si ha votado
            m.put("esFavorita", esFavorita); // Pasamos el estado de favorito al modelo

            System.out.println("liedhflidhsdf" + esFavorita);
            m.put("view", "pelicula/rDetailed");
            return "_t/frame";
        } else {
            // Si el usuario no está autenticado o no tiene el rol adecuado, redirígelo a la
            // página de inicio de sesión
            PRG.error("Debe estar registrado para acceder", "/pelicula/r");
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
            @RequestParam("urlCompra") String url, HttpSession session) throws DangerException, InfoException {
        Boolean creado = false;

        try {
            if (!ComentarioValidator.validarComentario(titulo)) {
                PRG.error("El titulo no puede tener palabras prohibidas", "/pelicula/r");
            }
            if (!ComentarioValidator.validarComentario(plataforma)) {
                PRG.error("La plataforma no puede tener palabras prohibidas", "/pelicula/r");
            }
            if (PeliculaValidator.ValidarDatos(titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                    idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url)) {
                peliculaService.update(idPelicula, titulo, clasificacion, duracion, estado, plataforma, puntuacion,
                        idsCategoria, sinopsis, fechaLanzamiento, cuentaVotos, trailer, url);
                // GESTION DE PUNTOS DEL USUARIO
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                String correoUsuario = usuario.getCorreo();
                usuarioService.modificacionPuntos(correoUsuario, 7);
            }
        } catch (Exception e) {
            PRG.error("Error al actualizar la película: " + e.getMessage(), "/pelicula/r");
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

        boolean esFavoritaAntes = false;
        for (Pelicula p : peliculasFav) {
            if (p.getIdElemento().equals(pelicula.getIdElemento())) {
                esFavoritaAntes = true;
                break;
            }
        }

        // Agregar la película a la lista de favoritos si no estaba presente antes
        if (!esFavoritaAntes) {
            usuarioService.saveUsuarioPeliculas(usuario, pelicula);
            String correoUsuario = usuario.getCorreo();
            usuarioService.modificacionPuntos(correoUsuario, 2);
        }

        // Verificar si la película está en la lista de favoritos después de agregarla
        boolean esFavorita = peliculasFav.contains(pelicula);

        m.put("pelicula", pelicula);
        m.put("esFavorita", esFavorita);

        // Redirigir a la página de detalles de la película
        return "redirect:/pelicula/rDetailed?id_elemento=" + pelicula.getIdElemento();
    }

    @PostMapping("rDetailedRating")
    public String puntuacion(
            @RequestParam("idPelicula") Long idPelicula,
            @RequestParam("rating") Long puntos,
            HttpSession session,
            ModelMap m) throws DangerException {

        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Voto voto = votoService.votarPelicula(usuario, pelicula, puntos);
        // usuarioService.saveUsuarioPeliculas(usuario, pelicula);
        String correoUsuario = usuario.getCorreo();
        usuarioService.modificacionPuntos(correoUsuario, 3);

        m.put("voto", voto.getPuntaje());
        m.put("pelicula", pelicula);
        m.put("calificacion", peliculaService.setCalificacion(usuario, pelicula, puntos));
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

    @PostMapping("eliminarFav")
    public String removeFavorite(
            @RequestParam("idPelicula") Long idPelicula,
            HttpSession session,
            ModelMap m) throws DangerException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/";
        }

        Pelicula pelicula = peliculaService.findByIdElemento(idPelicula);
        List<Pelicula> peliculasFav = usuario.getPeliculasFav();
        if (peliculasFav.contains(pelicula)) {
            usuarioService.removeUsuarioPeliculas(usuario, pelicula);
        }

        // System.out.println("ID de la película: " + pelicula.getIdElemento());
        // System.out.println("Título de la película: " + pelicula.getTitulo());

        m.put("pelicula", pelicula);

        return "redirect:/usuario/rDetailed";
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

    // @GetMapping("filtrar")
    // public String filtrar(@RequestParam(name = "idCategoria", required = false)
    // Long idCategoria,
    // @RequestParam(name = "clasificacion", required = false) String clasificacion,
    // ModelMap m) {
    // List<Pelicula> peliculasFiltradas;
    // List<Pelicula> peliculas = peliculaService.findAll();
    // List<String> clasificaciones = Arrays.asList("G", "PG", "R13", "R15", "M",
    // "R16", "RP16");
    // Categoria categoria = null;

    // if (idCategoria != null) {
    // categoria = categoriaService.findById(idCategoria);
    // }

    // if (idCategoria != null) {
    // peliculasFiltradas = new ArrayList<>();
    // for (Pelicula pelicula : peliculas) {
    // for (Categoria cat : pelicula.getCategorias()) {
    // if (cat.getIdCategoria().equals(idCategoria)) {
    // peliculasFiltradas.add(pelicula);
    // break;
    // }
    // }
    // }
    // } else if (clasificacion != null) {
    // peliculasFiltradas = new ArrayList<>();
    // for (Pelicula pelicula : peliculas) {
    // if (pelicula.getClasificacion().equals(clasificacion)) {
    // peliculasFiltradas.add(pelicula);
    // }
    // }
    // } else {
    // peliculasFiltradas = peliculas;
    // }

    // m.put("peliculas", peliculasFiltradas);
    // m.put("categorias", categoriaService.findAll());
    // m.put("clasificaciones", clasificaciones);
    // m.put("categoria", categoria);
    // m.put("view", "pelicula/r");
    // return "_t/frame";
    // }

    @GetMapping("filtrar")
    public String filtrar(@RequestParam(name = "idCategoria", required = false) Long idCategoria,
            @RequestParam(name = "clasificacion", required = false) String clasificacion,
            @RequestParam(defaultValue = "0") int page,
            ModelMap m) {
        List<Pelicula> peliculasFiltradas;
        List<Pelicula> peliculas = peliculaService.findAll();
        List<String> clasificaciones = Arrays.asList("R", "PG-13", "PG-16", "PG-17");
        Categoria categoria = null;

        if (idCategoria != null) {
            categoria = categoriaService.findById(idCategoria);

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

        // Crear un objeto Pageable específico para las películas filtradas
        Pageable pageable = PageRequest.of(page, 12);
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > peliculasFiltradas.size() ? peliculasFiltradas.size()
                : (start + pageable.getPageSize());
        Page<Pelicula> peliculasPage = new PageImpl<>(peliculasFiltradas.subList(start, end), pageable,
                peliculasFiltradas.size());

        m.put("peliculas", peliculasFiltradas);
        m.put("currentPage", page);
        m.put("categorias", categoriaService.findAll());
        m.put("clasificaciones", clasificaciones);
        m.put("totalPages", peliculasPage.getTotalPages());
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

    // MÉTODO PARA BUSCAR LA PELÍCULA
    /*
     * @GetMapping("/buscarPelicula")
     * public String buscarPelicula(@RequestParam("titulo") String titulo, ModelMap
     * m) {
     * // Realizar la búsqueda en la base de datos por el nombre de la película
     * List<Pelicula> peliculasEncontradas =
     * peliculaService.buscarPorNombre(titulo);
     * 
     * // Agregar las películas encontradas al modelo para mostrar en la vista
     * m.put("peliculasEncontradas", peliculasEncontradas);
     * 
     * // Devolver el nombre de la vista que mostrará los resultados de la búsqueda
     * return "resultado/rResultado"; // Este es el nombre de la vista que mostrará
     * los resultados
     * }
     */
}