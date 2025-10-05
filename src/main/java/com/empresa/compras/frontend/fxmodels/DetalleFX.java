package com.empresa.compras.frontend.fxmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleFX {

    private Long idOrden;
    private Long idInsumo;
    private String nombreInsumo;

    private final IntegerProperty cantidad = new SimpleIntegerProperty();
    private final DoubleProperty precioUnitario = new SimpleDoubleProperty();
    private final DoubleProperty subtotal = new SimpleDoubleProperty();

    public DetalleFX() {}

    public DetalleFX(Long idInsumo, String nombreInsumo, int cantidad, double precioUnitario, double subtotal) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.cantidad.set(cantidad);
        this.precioUnitario.set(precioUnitario);
        this.subtotal.set(subtotal);
    }

    // --- cantidad
    public int getCantidad() { return cantidad.get(); }
    public void setCantidad(int c) { this.cantidad.set(c); }
    public IntegerProperty cantidadProperty() { return cantidad; }

    // --- precioUnitario
    public double getPrecioUnitario() { return precioUnitario.get(); }
    public void setPrecioUnitario(double p) { this.precioUnitario.set(p); }
    public DoubleProperty precioUnitarioProperty() { return precioUnitario; }

    // --- subtotal
    public double getSubtotal() { return subtotal.get(); }
    public void setSubtotal(double s) { this.subtotal.set(s); }
    public DoubleProperty subtotalProperty() { return subtotal; }

    // --- nombre para mostrar en tabla
    public String getInsumo() {
        return nombreInsumo != null ? nombreInsumo : "Desconocido";
    }
}
