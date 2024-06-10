package org.proyect.helper;

import org.proyect.exception.DangerException;


public class CategoriaValidator {
    public static final String[] CATEGORIAS_PELICULA_VIDEOJUEGOS = {
            "Accion", "Aventura", "Comedia", "Drama", "Fantasia", "Terror", "Misterio",
            "Romance", "Ciencia ficcion", "Animacion", "Documental", "Musical",
            "Biografia", "Historia", "Deporte", "Familia", "Guerra", "Western", "RPG", "Shooter", "Plataforma",
            "Estrategia", "Carreras", "Puzzle", "Survival", "Simulación"
    };

    public static boolean validarCategoria(String nombreCategoria) throws DangerException {
        // Validar el nombre de la categoría
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty() || nombreCategoria.trim().length() < 3
                || nombreCategoria.trim().length() > 50
                || !nombreCategoria.matches("^[a-zA-Z0-9\\-\\s]+$")) {
            PRG.error(
                    "El nombre de la categoría debe tener entre 3 y 50 caracteres y solo puede contener letras, números y guiones.",
                    "/categoria/r");
        }

        // Validar si la categoría está en la lista de categorías permitidas
        boolean categoriaValida = false;
        for (String categoria : CATEGORIAS_PELICULA_VIDEOJUEGOS) {
            if (categoria.equalsIgnoreCase(nombreCategoria.trim())) {
                categoriaValida = true;
                break;
            }
        }

        if (!categoriaValida) {
            PRG.error("La categoría especificada no es válida.", "/categoria/r");
        }
        return true;
    }
}
