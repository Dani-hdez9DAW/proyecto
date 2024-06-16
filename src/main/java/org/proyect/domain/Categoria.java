package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column
    private String nombre;

    @ManyToMany(mappedBy = "categorias")
    private List<Pelicula> categoriaPelis;

    @ManyToMany(mappedBy = "categorias")
    private List<Juego> categoriaJuegos;

    // ==================

    public Categoria() {
        this.categoriaJuegos = new ArrayList<>();
        this.categoriaPelis = new ArrayList<>();
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.categoriaJuegos = new ArrayList<>();
        this.categoriaPelis = new ArrayList<>();
    }

}
