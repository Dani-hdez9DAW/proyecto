package org.proyect.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pelicula extends Elemento {

    @OneToMany(mappedBy = "pelicula")
    private List<Comentario> comentarios;

    @ManyToMany(mappedBy = "peliculasFav")
    private List<Usuario> usuarios;

    @ManyToMany
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "pelicula", fetch = FetchType.LAZY)
    private List<Voto> votos;

    // ==================

    public Pelicula(String titulo) {
        this.titulo = titulo;
        this.categorias = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.votos = new ArrayList<>();

    }

    public Pelicula(String titulo, String clasificacion, Integer duracion, String estado, String plataforma,
            String sinopsis, LocalDate fechaEstreno, String imagen, String trailer, String url) {
        this.titulo = titulo;
        this.categorias = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.puntuacion = 0;
        this.cuenta_votos = 0;
        this.fecha_salida = fechaEstreno;
        this.plataforma = plataforma;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
        this.estado = estado;
        this.sinopsis = sinopsis;
        this.imagen = imagen;
        this.trailer = trailer;
        this.url = url;
        this.votos = new ArrayList<>();

    }

    // ==================
    public Pelicula(String titulo, String clasificacion, Integer duracion, Integer puntuacion, String estado,
            String plataforma,
            String sinopsis, LocalDate fechaEstreno, String trailer, String url) {
        this.titulo = titulo;
        this.categorias = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.puntuacion = puntuacion;
        this.cuenta_votos = 0;
        this.fecha_salida = fechaEstreno;
        this.plataforma = plataforma;
        this.clasificacion = clasificacion;
        this.duracion = duracion;
        this.estado = estado;
        this.sinopsis = sinopsis;
        this.trailer = trailer;
        this.url = url;
        this.votos = new ArrayList<>();

    }

}
