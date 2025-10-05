package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados_orden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    @Column(nullable = false, unique = true, length = 50)
    private String nombreEstado;
}
