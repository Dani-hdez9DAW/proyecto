package org.proyect.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Admin extends Persona{

    @Column
    private Long id_admin;

        // ==================

    public Admin(Long id_admin) {
        this.id_admin = id_admin;
    }

}
