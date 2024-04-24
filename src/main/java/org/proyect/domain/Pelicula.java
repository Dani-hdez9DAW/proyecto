package org.proyect.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pelicula extends Elemento {

    @ManyToMany(mappedBy = "peliculas")
    private List<Actor> actores;

    @ManyToMany
    private List<Usuario> usuarios;

    @ManyToMany(mappedBy = "categoriaPelis")
    private List<Categoria> categorias;

    // ==================

    public Pelicula(String titulo) {
        this.titulo = titulo;
        this.actores = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public Pelicula(String titulo, String clasificacion, Integer duracion, String estado, String plataforma,
            String sinopsis, LocalDate fechaEstreno, String imagen, String trailer, String url) {
        this.titulo = titulo;
        this.actores = new ArrayList<>();
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

    }

}
