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
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_categoria;

    @Column
    private String nombre;


    @ManyToMany
    private List<Pelicula> categoriaPelis;

    @ManyToMany
    private List<Juego> categoriaJuegos;

    // ==================

    public Categoria(String nombre) {
        this.nombre = nombre;
        this.categoriaJuegos = new ArrayList<>();
        this.categoriaPelis = new ArrayList<>();
    }

}
