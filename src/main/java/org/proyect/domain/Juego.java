package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Juego extends Elemento {

    @Id
    private Long id_juego;

    private String plataforma;

    @ManyToMany
    private List<Usuario> usuarios;


    public Juego() {
        this.usuarios = new ArrayList<>();
    }

    public Juego(Long id_juego, String plataforma) {
        this.id_juego = id_juego;
        this.plataforma = plataforma;
        this.usuarios = new ArrayList<>();
    }
}
