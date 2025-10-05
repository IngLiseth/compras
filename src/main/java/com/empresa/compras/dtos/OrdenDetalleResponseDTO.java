package com.empresa.compras.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalleResponseDTO {

    private Long idOrden;
    private Long idInsumo;
    private String nombreInsumo;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
