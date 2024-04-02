package org.proyect.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Actor extends Persona{

    @Column
    private Long id_actor;

    @Column
    private String apellido;

    // ==================

    public Actor(Long id_actor,String  apellido) {
        this.id_actor = id_actor;
        this.apellido = apellido;
    }

}
