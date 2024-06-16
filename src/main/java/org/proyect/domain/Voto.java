package org.proyect.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_id")
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id")
    private Juego juego;

    @Column
    private Long puntaje;

    public Voto() {}

    public Voto(Usuario usuario, Pelicula pelicula, Long puntaje) {
        this.usuario = usuario;
        this.pelicula = pelicula;
        this.puntaje = puntaje;
    }

    public Voto(Usuario usuario, Juego juego, Long puntaje) {
        this.usuario = usuario;
        this.juego = juego;
        this.puntaje = puntaje;
    }
}