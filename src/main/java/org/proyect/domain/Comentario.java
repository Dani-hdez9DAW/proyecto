package org.proyect.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    private String autor;

    @Column
    private String contenido;

    @Column
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_id")
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id")
    private Juego juego;

    public Comentario(String autor, String contenido, LocalDate fecha, Pelicula pelicula) {
        this.autor = autor;
        this.contenido = contenido;
        this.fecha = fecha;
        this.pelicula = pelicula;
    }

    public Comentario(String autor, String contenido, LocalDate fecha, Juego juego) {
        this.autor = autor;
        this.contenido = contenido;
        this.fecha = fecha;
        this.juego = juego;
    }
}
