package org.proyect.domain;

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
public class Juego extends Elemento {

    @Column
    private Long id_juego;
    @Column
    private String plataforma;

    @ManyToMany(mappedBy = "juegosFav")
    private List<Usuario> usuarios;

    @ManyToMany(mappedBy = "categoriaJuegos")
    private List<Categoria> categorias;

    public Juego(Long id_juego, String plataforma) {
        this.id_juego = id_juego;
        this.plataforma = plataforma;
        this.usuarios = new ArrayList<>();
    }
}
