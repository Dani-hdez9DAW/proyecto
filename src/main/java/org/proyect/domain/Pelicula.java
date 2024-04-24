package org.proyect.domain;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Pelicula extends Elemento{

    @Column
    private Long id;

    @Column
    private String nombre;

    @ManyToMany(mappedBy = "peliculas")
    private List<Actor> actores;

    @ManyToMany
    private List<Usuario> usuarios;

    @ManyToMany(mappedBy="categoriaPelis")
    private List<Categoria> categorias;
    

    // ==================

    public Pelicula() {
        this.actores=new ArrayList<>();
    }

    public Pelicula(Long id, String nombre) {
        this.nombre = nombre;
        this.actores=new ArrayList<>();
    }

}
