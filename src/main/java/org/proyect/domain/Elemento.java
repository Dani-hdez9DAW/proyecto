package org.proyect.domain;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass

public abstract class Elemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_elemento;

    @Column
    private String titulo;

    @Column
    private String clasificacion;//Lo de pegi 7,etc 

    @Column
    private String sinopsis;

    @Column
    private Date fecha_salida;

    @Column
    private String plataforma;//Añadir en la base de datos

    @Column
    private String imagen;//Añadir en la base de datos

    @Column
    private String estado;

    @Column
    private Integer duracion;

    @Column
    private Integer puntuacion;

    @Column
    private Integer cuenta_votos;

    @Column
    private String trailer;


    @Column
    private String url;

    @ManyToMany
    private List<Categoria> categorias;

    // ==================

}
