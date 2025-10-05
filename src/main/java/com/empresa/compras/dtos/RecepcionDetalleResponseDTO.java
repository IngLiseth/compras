package com.empresa.compras.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionDetalleResponseDTO {

    private Long idInsumo;
    private String nombreInsumo;
    private Integer cantidadRecibida;
}
