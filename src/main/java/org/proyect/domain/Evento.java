package org.proyect.domain;

import java.sql.Date;
import java.time.LocalDate;

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
    private Long idEvento;

    @Column
    private String tipo;
    @Column
    private LocalDate fecha;
    @Column
    private String localizacion;
    @Column
    private String nombre;
    @ManyToOne
    private Usuario usuario;

    // ==================

    // Getters y Setters

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Evento(String tipo, LocalDate fecha, String localizacion, String nombre) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.localizacion = localizacion;
        this.nombre = nombre;
    }

    public Evento(String tipo) {
        this.tipo = tipo;
    }
}
