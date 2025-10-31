package com.empresa.compras.frontend.fxmodels;

import javafx.beans.property.*;

public  class PagoFX {
    private final LongProperty idPago = new SimpleLongProperty();
    private final StringProperty fecha = new SimpleStringProperty();
    private final LongProperty idOrden = new SimpleLongProperty();
    private final DoubleProperty montoTotal = new SimpleDoubleProperty();

    public PagoFX(Long idPago, String fecha, Long idOrden, Double montoTotal) {
        if (idPago != null) this.idPago.set(idPago);
        if (fecha != null) this.fecha.set(fecha);
        if (idOrden != null) this.idOrden.set(idOrden);
        if (montoTotal != null) this.montoTotal.set(montoTotal);
    }

    public long getIdPago() { return idPago.get(); }
    public LongProperty idPagoProperty() { return idPago; }

    public String getFecha() { return fecha.get(); }
    public StringProperty fechaProperty() { return fecha; }

    public long getIdOrden() { return idOrden.get(); }
    public LongProperty idOrdenProperty() { return idOrden; }

    public double getMontoTotal() { return montoTotal.get(); }
    public DoubleProperty montoTotalProperty() { return montoTotal; }
}

