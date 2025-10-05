package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInsumo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombreInsumo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String unidadMedida;

    @Column(nullable = false)
    private Double precioUnitario;
}
