package org.proyect.domain;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Pelicula extends Elemento {

    @Column
    private Long id;

    @Column
    private String nombre;

    @ManyToMany(mappedBy = "pelicula")
    private Collection<Actor> actores;

    @ManyToMany
    private Usuario usuarios;

    // ==================

    public Pelicula() {
        this.actores=new ArrayList<>();
    }

    public Pelicula(Long id, String nombre) {
        this.nombre = nombre;
        this.actores=new ArrayList<>();
    }

}
