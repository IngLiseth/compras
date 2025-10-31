package com.empresa.compras.dtos;

import lombok.*;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDTO {
    private LocalDate fecha;
    private Double montoTotal;
    private Long idOrden;
    private List<PagoDetalleRequestDTO> detalles; // ✅ ESTO FALTA EN TU CÓDIGO
}
