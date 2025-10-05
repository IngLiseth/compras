package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.IdGeneratorType;

@Entity
@Table(name="roles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(nullable = false, unique = true)
    private String nombreRol;


}



