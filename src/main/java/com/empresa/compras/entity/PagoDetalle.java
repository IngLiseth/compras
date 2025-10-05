package com.empresa.compras.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagos_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @Column(name = "monto_parcial")
    private Double montoParcial;

    // FK → pagos
    @ManyToOne
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    private Pago pago;

    // FK → metodos_pago
    @ManyToOne
    @JoinColumn(name = "id_metodo_pago", referencedColumnName = "id_metodo_pago")
    private MetodoPago metodoPago;
}
