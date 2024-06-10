package org.proyect.helper;

import java.util.Random;

public class SistemaPuntuacion {
    private static final Random random = new Random();

    public static int generarPuntos(Integer puntuacionMaxima) {
        return random.nextInt(puntuacionMaxima) + 1;
    }
}