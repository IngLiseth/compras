package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recepciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Long idRecepcion;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private OrdenCompra orden;

    @Column(nullable = false)
    private LocalDate fecha;

    private String observaciones;

    // Relación con detalles de recepción
    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecepcionDetalle> detalles = new ArrayList<>();
}
