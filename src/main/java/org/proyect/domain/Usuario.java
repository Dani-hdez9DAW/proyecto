package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Usuario extends Persona {

    @Column
    private Long puntos;

    @Column
    private Long descuento;
    

    @ManyToMany(mappedBy = "usuarios")
    private List<Pelicula> peliculasFav;

    @ManyToMany
    private List<Juego> juegosFav;

    @Column
    private Boolean estaRegistrado;

    @Column
    private Boolean esAdmin;

    // ==================

    public Usuario() {
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
    }

    public Usuario(String nombre, String passwd) {
        super.setNombre(nombre); // Usar el setter heredado de Persona
        super.setContraseña(passwd);
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
    }

    public Usuario(Long puntos, Long descuento) {

        this.puntos = puntos;
        this.descuento = descuento;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
    }

}
