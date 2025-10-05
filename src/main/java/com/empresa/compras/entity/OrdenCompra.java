package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long idOrden;

    @Column(nullable = false)
    private LocalDate fecha;

    private String observaciones;

    // 🔹 Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // 🔹 Relación con Proveedor
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    // 🔹 Relación con Estado de la orden
    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoOrden estado;

    // 🔹 Relación con los detalles de la orden
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrdenDetalle> detalles = new ArrayList<>();

}
