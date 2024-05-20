package org.proyect.controller.web;

import java.util.List;

import org.proyect.domain.Juego;
import org.proyect.domain.Pelicula;
import org.proyect.domain.Usuario;
import org.proyect.exception.DangerException;
import org.proyect.helper.EmailValidator;
import org.proyect.helper.PRG;
import org.proyect.service.JuegoService;
import org.proyect.service.PeliculaService;
import org.proyect.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PeliculaService peliculaService;
	@Autowired
	private JuegoService juegoService;

	@GetMapping("/")
	public String home(ModelMap m) {
		List<Juego> juegos = juegoService.findAll();
		int cantidadMaximaJuegos = 4;
		m.put("juegos", juegos);
		m.addAttribute("cantidadMaximaJuegos", cantidadMaximaJuegos);

		List<Pelicula> peliculas = peliculaService.findAll();
		int cantidadMaximaPeliculas = 4;
		m.put("peliculas", peliculas);
		m.addAttribute("cantidadMaximaPeliculas", cantidadMaximaPeliculas);

		// Agrega las películas para el carrusel
		m.put("carouselPeliculas", peliculas.subList(0, Math.min(4, peliculas.size())));

		m.put("view", "home/home");
		return "_t/frame";
	}

	@GetMapping("/info")
	public String info(HttpSession s, ModelMap m) {

		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";

		s.removeAttribute("_mensaje");
		s.removeAttribute("_severity");
		s.removeAttribute("_link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "/_t/info");
		return "/_t/frame";
	}

	@GetMapping("/init")
	public String crearAdmin() {
		usuarioService.save("admin", "admin", null);
		usuarioService.setAdmin("-1");
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login(
			ModelMap m) {
		m.put("view", "home/login");
		return "_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(
			@RequestParam("email") String email,
			@RequestParam("pass") String password,
			HttpSession s,
			ModelMap m) throws DangerException {
		try {
			// Validar el formato del correo electrónico
			if (!EmailValidator.isValidEmail(email)) {
				PRG.error("Formato de correo electrónico no válido");
			}

			Usuario usuario = usuarioService.login(email, password);
			usuarioService.setRegistro(email);
			s.setAttribute("usuario", usuario);
			s.setAttribute("nombre", email);

		} catch (Exception e) {
			PRG.error("Usuario o contraseña incorrectos");
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession s) {
		String nombre = (String) s.getAttribute("nombre");
		usuarioService.setLogout(nombre);
		s.invalidate();
		return "redirect:/";
	}

}