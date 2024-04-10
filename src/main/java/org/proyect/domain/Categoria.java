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
public class Categoria{

    @Column
    private Long id_categoria;

    @Column
    private String nombre;
    @Column
    private String tipo;
    @ManyToMany(mappedBy="categorias")
    private List<Elemento>elementos;



    // ==================

    public Categoria(Long id_categoria, String nombre, String tipo) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.tipo= tipo;
        this.elementos=new ArrayList<>();
    }

}
