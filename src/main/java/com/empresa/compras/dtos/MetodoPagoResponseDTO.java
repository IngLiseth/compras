package com.empresa.compras.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoResponseDTO {
    private Long idMetodoPago;
    private String nombreMetodo;
}

