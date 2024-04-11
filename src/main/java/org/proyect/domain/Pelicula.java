package org.proyect.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Pelicula{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @ManyToMany(mappedBy = "peliculas")
    private List<Actor> actores;

    @ManyToMany
    private List<Usuario> usuarios;

    

    // ==================

    public Pelicula() {
        this.actores=new ArrayList<>();
    }

    public Pelicula(Long id, String nombre) {
        this.nombre = nombre;
        this.actores=new ArrayList<>();
    }

}
