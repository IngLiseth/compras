package com.empresa.compras.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalleId implements Serializable {

    private Long idOrden;
    private Long idInsumo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenDetalleId)) return false;
        OrdenDetalleId that = (OrdenDetalleId) o;
        return Objects.equals(idOrden, that.idOrden) &&
                Objects.equals(idInsumo, that.idInsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrden, idInsumo);
    }
}
