package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @Column(name = "id_insumo")
    private Long idInsumo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_insumo")
    private Insumo insumo;

    @Column(name = "stock_actual", nullable = false)
    private int stockActual;

    // 🔹 Constructor personalizado para inicializar con insumo y stock inicial
    public Inventario(Insumo insumo, int stockActual) {
        this.insumo = insumo;
        this.idInsumo = insumo.getIdInsumo(); // importante porque usas @MapsId
        this.stockActual = stockActual;
    }
}
