package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    // @Lob
    // @Column(name = "foto_perfil")
    // private byte[] fotoPerfil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_pelicula", joinColumns = @JoinColumn(name = "usuario_idPersona"), inverseJoinColumns = @JoinColumn(name = "pelicula_idElemento"))
    private List<Pelicula> peliculasFav;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_juego", joinColumns = @JoinColumn(name = "usuario_idPersona"), inverseJoinColumns = @JoinColumn(name = "juego_idElemento"))
    private List<Juego> juegosFav;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Voto> votos;

    @Column
    private Boolean estaRegistrado;

    @Column
    private Boolean esAdmin;

    // ==================

    public Usuario() {
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
        this.votos = new ArrayList<>();

    }

    public Usuario(String nombre, String passwd, String correo, Integer puntos) {
        super.setNombre(nombre);
        super.setContraseña(passwd);
        super.setCorreo(correo);
        this.puntos = puntos;
        this.esAdmin = false;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
        this.votos = new ArrayList<>();


    }

    public Usuario(String nombre, String passwd, String correo, Integer puntos, Boolean esAdmin) {
        super.setNombre(nombre);
        super.setContraseña(passwd);
        super.setCorreo(correo);
        this.puntos = puntos;
        this.esAdmin = esAdmin;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
        this.votos = new ArrayList<>();

    }

    public Usuario(Integer puntos, Long descuento) {

        this.puntos = puntos;
        this.descuento = descuento;
        this.peliculasFav = new ArrayList<>();
        this.juegosFav = new ArrayList<>();
        this.votos = new ArrayList<>();

    }

    public Boolean getEsAdmin() {
        return esAdmin != null ? esAdmin : false;
    }

}
