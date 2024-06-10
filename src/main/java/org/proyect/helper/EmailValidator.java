package org.proyect.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.proyect.exception.DangerException;

public class EmailValidator {

    // Expression that allows for a more flexible email validation
    private static final String EMAIL_REGEX = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static String[] palabrasProhibidas = {
            "tonto", "idiota", "imbecil", "estupido", "burro",
            "payaso", "loco", "tarado", "subnormal", "bestia",
            "inútil", "patán", "cretino", "baboso", "bobo",
            "bruto", "estúpido", "necio", "pesado", "zopenco",
            "menso", "gilipollas", "cabrón", "capullo", "cachondeo",
            "hp", "puta", "puto", "zorra", "hijo de puta", "atontado", "z0rra", "zoquete"
    };

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        // Check if email matches the pattern
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            return false;
        }

        // Check if email contains any prohibited words
        for (String palabra : palabrasProhibidas) {
            if (email.toLowerCase().contains(palabra)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidNamePass(String nombre, String pass) throws DangerException {
        if (nombre == null || nombre.trim().isEmpty() || nombre.trim().length() < 2 || nombre.trim().length() > 12) {
            PRG.error("El nombre debe tener entre 2 y 12 caracteres y solo puede contener letras, números y guiones.",
                    "redirect:/");
        }
        if (pass == null || pass.isEmpty() || pass.trim().length() < 4 || pass.trim().length() > 15) {
            PRG.error("La contraseña no puede estar vacia y debe tener entre 4 y 15 caracteres", "redirect:/");

        }
        return true;
    }
}
