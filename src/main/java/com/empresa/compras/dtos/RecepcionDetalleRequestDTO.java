package com.empresa.compras.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecepcionDetalleRequestDTO {

    @NotNull(message = "Debe indicar el insumo recibido")
    private Long idInsumo;

    @NotNull(message = "Debe indicar la cantidad recibida")
    private Integer cantidadRecibida;
}
