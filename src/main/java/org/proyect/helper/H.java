package org.proyect.helper;


import org.proyect.domain.Usuario;

import jakarta.servlet.http.HttpSession;

public class H {
   
    private static int rolIndex(String rol) {
        int index = 0;
        switch (rol) {
            case "auth":
                index = 1;
                break;
            case "admin":
                index = 2;
                break;
            default:
                break;
        }
        return index;
    }
     /**
     * 
     * @param rol anon,auth,admin
     * @return
     */

    public static boolean isRolOk(String rolRequerido, HttpSession s) {
        String rolActual = "anon";
        if (s.getAttribute("usuario") != null) {
            Usuario u = (Usuario) s.getAttribute("usuario");
            if (u.getEsAdmin()) {
                rolActual = "admin";
            } else {
                rolActual = "auth";
            }
        }
        return rolIndex(rolActual) >= rolIndex(rolRequerido);
    }
}
