package org.proyect.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Juego extends Elemento {

    @Column
    private String plataforma;

    @OneToMany(mappedBy = "juego")
    private List<Comentario> comentarios;

    @ManyToMany(mappedBy = "juegosFav")
    private List<Usuario> usuarios;

    @ManyToMany
    private List<Categoria> categorias;

    public Juego(String plataforma) {
        this.usuarios = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.categorias = new ArrayList<>();
    }

    public Juego(String titulo, String clasificacion, Integer duracion, Integer puntuacion, String estado,
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

    }
}
