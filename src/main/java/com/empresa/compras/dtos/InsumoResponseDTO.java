package com.empresa.compras.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsumoResponseDTO {
    private Long idInsumo;
    private String nombreInsumo;
    private String descripcion;
    private String unidadMedida;
    private Double precioUnitario;
}
