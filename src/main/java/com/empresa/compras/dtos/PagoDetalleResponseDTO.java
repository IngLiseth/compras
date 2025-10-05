package com.empresa.compras.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDetalleResponseDTO {
    private Long idDetalle;
    private String metodoPago; // nombre del método
    private Double montoParcial;
}
