package org.proyect.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Actor extends Persona{

    @Column
    private Long id_actor;

    @Column
    private String apellido;

    @ManyToMany
    private List<Pelicula> pelicula;


    // ==================

    public Actor() {
        this.pelicula=new ArrayList<>();
    }

    public Actor(Long id_actor,String  apellido) {
        this.id_actor = id_actor;
        this.apellido = apellido;
        this.pelicula=new ArrayList<>();
    }

}
