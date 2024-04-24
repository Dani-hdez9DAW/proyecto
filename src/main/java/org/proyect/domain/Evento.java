package org.proyect.domain;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_evento;

    @Column
    private String tipo;
    @Column
    private Date fecha;
    @Column
    private String localizacion;
    @ManyToOne
    private Usuario usuario;

    // ==================

    public Evento(String tipo, Date fecha, String localizacion) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.localizacion = localizacion;
    }

}
