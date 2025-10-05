package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recepciones_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionDetalle {

    @EmbeddedId
    private RecepcionDetalleId id;

    @ManyToOne
    @MapsId("idRecepcion")
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;

    @ManyToOne
    @MapsId("idInsumo")
    @JoinColumn(name = "id_insumo")
    private Insumo insumo;

    private Integer cantidadRecibida;
}
