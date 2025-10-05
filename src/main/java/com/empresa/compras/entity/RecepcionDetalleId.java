package com.empresa.compras.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode   // 🔹 muy importante para ids compuestos
public class RecepcionDetalleId implements Serializable {

    private Long idRecepcion;
    private Long idInsumo;
}
