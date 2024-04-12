package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Actor extends Persona {

    @Column
    private Long id_actor;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @ManyToMany
    private List<Pelicula> peliculas;

    // ==================

    public Actor() {
        this.peliculas = new ArrayList<>();
    }

    public Actor(String nombre) {
        this.nombre = nombre;
        this.peliculas = new ArrayList<>();

    }

    public Actor(Long id_actor, String nombre, String apellido) {
        this.id_actor = id_actor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.peliculas = new ArrayList<>();
    }

}
