package com.empresa.compras.frontend.fxmodels;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DetallePagoFX {

    private Long idDetalle;
    private StringProperty metodoPago = new SimpleStringProperty();
    private DoubleProperty montoParcial = new SimpleDoubleProperty();

    // Constructor vacío
    public DetallePagoFX() {}

    // Constructor con datos
    public DetallePagoFX(Long idDetalle, String metodoPago, Double montoParcial) {
        this.idDetalle = idDetalle;
        this.metodoPago.set(metodoPago);
        this.montoParcial.set(montoParcial);
    }

    // Getters normales
    public Long getIdDetalle() { return idDetalle; }
    public String getMetodoPago() { return metodoPago.get(); }
    public Double getMontoParcial() { return montoParcial.get(); }

    // Properties para JavaFX table
    public StringProperty metodoPagoProperty() { return metodoPago; }
    public DoubleProperty montoParcialProperty() { return montoParcial; }

    // Setters
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public void setMetodoPago(String metodoPago) { this.metodoPago.set(metodoPago); }
    public void setMontoParcial(Double montoParcial) { this.montoParcial.set(montoParcial); }
}
