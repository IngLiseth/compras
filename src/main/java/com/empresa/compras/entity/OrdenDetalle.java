package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ordenes_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalle {

    @EmbeddedId
    private OrdenDetalleId id; // si usas clave compuesta

    @ManyToOne
    @MapsId("idOrden")
    @JoinColumn(name = "id_orden")
    private OrdenCompra orden;

    @ManyToOne
    @MapsId("idInsumo")
    @JoinColumn(name = "id_insumo")
    private Insumo insumo;

    @Column(nullable = false)
    private Integer cantidad;
    @Column(nullable = false)
    private Double precioUnitario;

    public Double getSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            return cantidad * precioUnitario;
        }
        return 0.0;
    }
}
