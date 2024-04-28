package org.proyect.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;


@MappedSuperclass
@Data
public abstract class Elemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_elemento;

    @Column
    protected String titulo;

    @Column
    protected String clasificacion;//Lo de pegi 7,etc 

    @Column
    protected String sinopsis;

    @Column
    protected LocalDate fecha_salida;

    @Column
    protected String plataforma;//Añadir en la base de datos

    @Column
    protected String imagen;//Añadir en la base de datos

    @Column
    protected String estado;

    @Column
    protected Integer duracion;

    @Column
    protected Integer puntuacion;

    @Column
    protected Integer cuenta_votos;

    @Column
    protected String trailer;


    @Column
    protected String url;

    @ManyToMany
    private List<Categoria> categorias;

    // ==================

}
