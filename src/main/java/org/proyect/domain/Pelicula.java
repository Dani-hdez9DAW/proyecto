package org.proyect.domain;


import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Pelicula extends Elemento {

    @Column
    private Long id;

    @Column
    private String nombre;

    @OneToMany(mappedBy = "actores")
    private Collection<Actor> actores;

    // ==================

    public Pelicula() {
        this.actores=new ArrayList<>();
    }

    public Pelicula(Long id, String nombre) {
        this.nombre = nombre;
        this.actores=new ArrayList<>();
    }

}
