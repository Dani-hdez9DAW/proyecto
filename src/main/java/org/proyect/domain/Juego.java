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
public class Juego extends Elemento  {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_juego;
    @Column
    private String plataforma;

    @ManyToMany(mappedBy = "juegosFav")
    private List<Usuario> usuarios;

    @ManyToMany(mappedBy="categoriaJuegos")
    private List<Categoria>categorias;


    public Juego(Long id_juego, String plataforma) {
        this.id_juego = id_juego;
        this.plataforma = plataforma;
        this.usuarios = new ArrayList<>();
    }
}
