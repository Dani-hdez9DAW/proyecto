package org.proyect.helper;

import java.time.LocalDate;
import java.util.List;

import org.proyect.exception.DangerException;
import org.springframework.web.multipart.MultipartFile;

public class PeliculaValidator {
    public static boolean ValidarDatos(String titulo, String clasificacion, Integer duracion, String estado,
            String plataforma,
            Integer puntuacion, List<Long> idsCategoria, String sinopsis, LocalDate fechaLanzamiento,
            Integer cuentaVotos, String trailer, String url) throws DangerException {
        // Validar el nombre
        if (titulo == null || titulo.trim().isEmpty() || titulo.trim().length() < 2 || titulo.trim().length() > 30
                || !titulo.matches("^[a-zA-Z0-9\\-\\s]+$")) {
            PRG.error("El nombre debe tener entre 2 y 30 caracteres y solo puede contener letras, números y guiones.",
                    "/pelicula/r");
        }

        // Validar la clasificación
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            PRG.error("La clasificación es obligatoria", "/pelicula/r");
        }

        // Validar la duración
        if (duracion == null || duracion < 10 || duracion > 500) {
            PRG.error("La duración debe estar entre 10 y 500 minutos.", "/pelicula/r");
        }

        // Validar el estado
        if (estado == null || estado.trim().isEmpty()) {
            PRG.error("El estado es obligatorio.", "/pelicula/r");
        }

        // Validar la plataforma
        if (plataforma == null || plataforma.trim().isEmpty()) {
            PRG.error("La plataforma es obligatoria.", "/pelicula/r");
        }

        // Validar la puntuación
        if (puntuacion == null || puntuacion < 0 || puntuacion > 10) {
            PRG.error("La puntuación debe estar entre 0 y 10.", "/pelicula/r");
        }

        // Validar la sinopsis
        if (sinopsis == null || sinopsis.trim().length() < 50 || sinopsis.trim().length() > 500) {
            PRG.error("La sinopsis debe tener entre 50 y 500 caracteres.", "/pelicula/r");
        }

        

        // Validar la cuenta de votos
        if (cuentaVotos == null || cuentaVotos < 0) {
            PRG.error("La cuenta de votos debe ser al menos 0.", "/pelicula/r");
        }

        // Validar el trailer
        if (trailer == null || !trailer.matches("^https://www\\.youtube\\.com/embed/.*$")) {
            PRG.error("El enlace del trailer debe tener esta estructura: https://www.youtube.com/embed/",
                    "/pelicula/r");
        }

        // Validar la URL de compra
        if (url == null || !url.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            PRG.error("La URL de compra debe ser una URL válida.", "/pelicula/r");
        }
        return true;
    }

    // CASO DE C DE PELIS
    public static boolean ValidarDatosC(String titulo, String clasificacion, Integer duracion, String estado,
            String plataforma,
            Integer puntuacion, List<Long> categoria, String sinopsis, LocalDate fechaLanzamiento,
            Integer cuentaVotos, String trailer, String url, MultipartFile imagen) throws DangerException {
        // Validar el nombre
        if (titulo == null || titulo.trim().isEmpty() || titulo.trim().length() < 2 || titulo.trim().length() > 30
        || !titulo.matches("^[a-zA-Z0-9\\-\\s]+$")) {
    PRG.error("El nombre debe tener entre 2 y 30 caracteres y solo puede contener letras, números y guiones.",
            "/pelicula/r");
}

        // Validar la clasificación
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            PRG.error("La clasificación es obligatoria", "/pelicula/r");
        }

        // Validar la duración
        if (duracion == null || duracion < 10 || duracion > 500) {
            PRG.error("La duración debe estar entre 10 y 500 minutos.", "/pelicula/r");
        }

        // Validar el estado
        if (estado == null || estado.trim().isEmpty()) {
            PRG.error("El estado es obligatorio.", "/pelicula/r");
        }

        // Validar la plataforma
        if (plataforma == null || plataforma.trim().isEmpty()) {
            PRG.error("La plataforma es obligatoria.", "/pelicula/r");
        }

        // Validar la puntuación
        if (puntuacion == null || puntuacion < 0 || puntuacion > 10) {
            PRG.error("La puntuación debe estar entre 0 y 10.", "/pelicula/r");
        }

        // Validar la sinopsis
        if (sinopsis == null || sinopsis.trim().length() < 50 || sinopsis.trim().length() > 500) {
            PRG.error("La sinopsis debe tener entre 50 y 500 caracteres.", "/pelicula/r");
        }

        

        // Validar la cuenta de votos
        if (cuentaVotos == null || cuentaVotos < 0) {
            PRG.error("La cuenta de votos debe ser al menos 0.", "/pelicula/r");
        }

        // Validar el trailer
        if (trailer == null || !trailer.matches("^https://www\\.youtube\\.com/embed/.*$")) {
            PRG.error("El enlace del trailer debe tener esta estructura: https://www.youtube.com/embed/",
                    "/pelicula/r");
        }

        // Validar la URL de compra
        if (url == null || !url.matches("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")) {
            PRG.error("La URL de compra debe ser una URL válida.", "/pelicula/r");
        }

        // Validar la imagen
        if (imagen == null || imagen.isEmpty()) {
            PRG.error("La imagen es obligatoria.", "/pelicula/r");
        } else {
            String contentType = imagen.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")
                    && !contentType.equals("image/jpg")) {
                PRG.error("La imagen debe ser un archivo JPEG, JPG o PNG.", "/pelicula/r");
            }

            // Validar el tamaño de la imagen (por ejemplo, no más de 2MB)
            long maxSize = 2 * 1024 * 1024; // 2MB
            if (imagen.getSize() > maxSize) {
                PRG.error("La imagen no debe ser mayor de 2MB.", "/pelicula/r");
            }
        }
        return true;
    }
}
