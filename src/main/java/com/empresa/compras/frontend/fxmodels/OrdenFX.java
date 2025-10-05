package com.empresa.compras.frontend.fxmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // 👈 Ignora campos extra
public class OrdenFX {

    private Long idOrden;
    private StringProperty usuario = new SimpleStringProperty();
    private StringProperty proveedor = new SimpleStringProperty();
    private StringProperty estado = new SimpleStringProperty();
    private StringProperty observaciones = new SimpleStringProperty();
    private DoubleProperty total = new SimpleDoubleProperty();

    // --- detalles
    // 🔹 Aquí incluimos el arreglo de detalles
    @Setter
    @Getter
    private List<DetalleFX> detalles;

    public OrdenFX() {}

    // --- idOrden
    public Long getIdOrden() { return idOrden; }
    public void setIdOrden(Long idOrden) { this.idOrden = idOrden; }
    public LongProperty idOrdenProperty() { return new SimpleLongProperty(idOrden); }

    // --- usuario
    public String getUsuario() { return usuario.get(); }
    public void setUsuario(String u) { this.usuario.set(u); }
    public StringProperty usuarioProperty() { return usuario; }

    // --- proveedor
    public String getProveedor() { return proveedor.get(); }
    public void setProveedor(String p) { this.proveedor.set(p); }
    public StringProperty proveedorProperty() { return proveedor; }

    // --- estado
    public String getEstado() { return estado.get(); }
    public void setEstado(String e) { this.estado.set(e); }
    public StringProperty estadoProperty() { return estado; }

    // --- observaciones
    public String getObservaciones() { return observaciones.get(); }
    public void setObservaciones(String o) { this.observaciones.set(o); }
    public StringProperty observacionesProperty() { return observaciones; }

    // --- total
    public double getTotal() { return total.get(); }
    public void setTotal(double t) { this.total.set(t); }
    public DoubleProperty totalProperty() { return total; }

}
