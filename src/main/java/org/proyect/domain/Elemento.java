package org.proyect.domain;


import java.sql.Date;
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
public class Elemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_elemento;

    @Column
    private String titulo;

    @ManyToMany
    private List<Categoria> categorias;

    @Column
    private String clasificacion;

    @Column
    private String sinopsis;

    @Column
    private Date fechaSalida;

    @Column
    private String plataforma;

    @Column
    private String imagen;

    @Column
    private String url;

    // ==================



}
