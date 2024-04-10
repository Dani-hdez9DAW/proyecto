package org.proyect.domain;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Usuario extends Persona{

    @Column
    private Long id_usuario;

    @Column
    private Long puntos;

    @Column
    private Long descuento;

    @ManyToMany(mappedBy = "usuarios")
    private Collection<Pelicula> peliculasFav;

    @ManyToMany(mappedBy = "usuarios")
    private Collection<Juego> juegosFav;

    @Column
    private Boolean estaRegistrado;

    @Column
    private Boolean esAdmin;

    // ==================

    public Usuario() {
        this.peliculasFav=new ArrayList<>();
        this.juegosFav=new ArrayList<>();
    }

    public Usuario(Long id_usuario, Long puntos, Long descuento) {
        this.id_usuario = id_usuario;
        this.puntos = puntos;
        this.descuento = descuento;
        this.peliculasFav=new ArrayList<>();
        this.juegosFav=new ArrayList<>();
    }

}
