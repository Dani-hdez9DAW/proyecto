package org.proyect.domain;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column
    private String categoria;

    @Column
    private String edad;

    @Column
    private String sinopsis;

    @Column
    private Date fechaSalida;

    @Column
    private String lugarDisponible;

    @Column
    private String creador;


    // ==================



}
