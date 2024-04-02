package org.proyect.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Juego extends Elemento{

    @Column
    private Long id_juego;

    @Column
    private String plataforma;


    // ==================

    public Juego(Long id_juego, String plataforma) {
        this.id_juego = id_juego;
        this.plataforma = plataforma;
    }

}
