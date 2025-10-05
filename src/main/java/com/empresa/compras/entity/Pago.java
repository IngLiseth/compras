package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @ManyToOne
    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden")
    private OrdenCompra orden;


    // Relación con detalles de pago
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoDetalle> detalles = new ArrayList<>();
}
