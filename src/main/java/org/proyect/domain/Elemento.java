package org.proyect.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
    private Long idElemento;

    @Column
    protected String titulo;

    @Column
    protected String clasificacion; 

    @Column
    protected String sinopsis;

    @Column
    protected LocalDate fecha_salida;

    @Column
    protected String plataforma; 

    @Column
    protected String imagen; 

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

    @Column
    protected Long calificacion;

    @ElementCollection
    private List<Long> calificaciones;

    // ==================

}
