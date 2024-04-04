package org.proyect.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToMany
    private Usuario usuarios;


    // ==================

    public Juego(Long id_juego, String plataforma) {
        this.id_juego = id_juego;
        this.plataforma = plataforma;
    }

}
