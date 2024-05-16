package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Usuario extends Persona {

    @Column
    private Integer puntos;

    @Column
    private Long descuento;

    @Column
    private String descripcion;
    

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_pelicula",
        joinColumns = @JoinColumn(name = "usuario_idPersona"),
        inverseJoinColumns = @JoinColumn(name = "pelicula_idElemento"))
    private List<Pelicula> peliculasFav;

    @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(
    //     name = "usuario_pelicula",
    //     joinColumns = @JoinColumn(name = "usuario_idPersona"),
    //     inverseJoinColumns = @JoinColumn(name = "juego_idElemento"))
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


    public Usuario(String nombre, String passwd,String correo,Integer puntos) {
        super.setNombre(nombre); // Usar el setter heredado de Persona
        super.setContraseña(passwd);
        super.setCorreo(correo);
        this.puntos = puntos;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
    }

    public Usuario(Integer puntos, Long descuento) {

        this.puntos = puntos;
        this.descuento = descuento;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
    }
    public Boolean getEsAdmin() {
        // Manejo del caso en el que esAdmin sea nulo
        return esAdmin != null ? esAdmin : false; // Devuelve false si esAdmin es nulo
    }

}
