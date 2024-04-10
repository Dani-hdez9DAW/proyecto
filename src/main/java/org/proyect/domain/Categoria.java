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
public class Categoria{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_categoria;

    @Column
    private Long id;

    @Column
    private String nombre;

    @Column
    private String tipo;

    @ManyToMany(mappedBy="categorias")
    private List<Elemento>elementos;


    // ==================

    public Categoria() {
        this.elementos=new ArrayList<>();
    }

    public Categoria(Long id_categoria, String nombre, String tipo) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.tipo= tipo;
        this.elementos=new ArrayList<>();
    }

}
