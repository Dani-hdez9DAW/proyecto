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
    private String plataforma;

    @ManyToMany(mappedBy = "juegosFav")
    private List<Usuario> usuarios;

    @ManyToMany(mappedBy = "categoriaJuegos")
    private List<Categoria> categorias;

    public Juego(String plataforma) {
        this.plataforma = plataforma;
        this.usuarios = new ArrayList<>();
    }
}
