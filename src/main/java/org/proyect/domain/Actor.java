package org.proyect.domain;


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
public class Actor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_actor;

    @Column
    private String apellido;

    @ManyToMany
    private List<Pelicula>peliculas;




    // ==================



    public Actor(Long id_actor,String  apellido) {
        this.id_actor = id_actor;
        this.apellido = apellido;
      
    }

}
