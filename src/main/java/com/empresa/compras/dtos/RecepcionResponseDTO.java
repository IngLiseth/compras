package com.empresa.compras.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionResponseDTO {

    private Long idRecepcion;
    private LocalDate fecha;
    private String observaciones;
    private Long idOrden;   // referencia a la orden
    private List<RecepcionDetalleResponseDTO> detalles;
}
