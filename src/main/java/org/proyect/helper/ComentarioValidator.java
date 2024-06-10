package org.proyect.helper;

public class ComentarioValidator {
    private static String[] palabrasProhibidas = {
            "tonto", "idiota", "imbecil", "estupido", "burro",
            "payaso", "loco", "tarado", "subnormal", "bestia",
            "inútil", "patán", "cretino", "baboso", "bobo",
            "bruto", "estúpido", "necio", "pesado", "zopenco",
            "menso", "gilipollas", "cabrón", "capullo", "cachondeo",
            "hp", "puta", "puto", "zorra", "hijo de puta", "atontado", "z0rra", "zoquete", "pilila", "chocho", "coño",
            "suicidate", "pene", "muerete", "vagina", "polla", "tus muertos", "ojala te mueras", "puto", "puton"
    };

    private static boolean esPalabrota(String palabra) {
        for (String palabrota : palabrasProhibidas) {
            if (palabrota.equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    public static boolean validarComentario(String comentario) {
        // Verificar si el comentario es nulo o vacío
        if (comentario == null || comentario.trim().isEmpty()) {
            return false;
        }

        // Dividir el comentario en palabras y verificar cada una
        String[] palabras = comentario.split("\\s+");
        for (String palabra : palabras) {
            if (esPalabrota(palabra)) {
                return false;
            }
        }

        return true;
    }
}
