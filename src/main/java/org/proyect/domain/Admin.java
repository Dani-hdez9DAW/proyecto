package org.proyect.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Admin extends Persona {

    @Column
    private Long id_admin;

    @OneToMany(mappedBy = "admin")
    private List<Evento> eventos;

    // ==================

    public Admin(Long id_admin) {
        this.id_admin = id_admin;
    }

}
