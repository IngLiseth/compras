package com.empresa.compras.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String observaciones;

    @NotNull(message = "Debe indicar la orden asociada")
    private Long idOrden;

    @NotNull(message = "Debe incluir al menos un detalle")
    private List<RecepcionDetalleRequestDTO> detalles;
}
