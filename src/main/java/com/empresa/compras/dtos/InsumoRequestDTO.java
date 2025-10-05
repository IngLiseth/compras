package com.empresa.compras.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequestDTO {

    @NotBlank(message = "El nombre del insumo es obligatorio")
    private String nombreInsumo;

    private String descripcion;

    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unidadMedida;

    @NotNull(message = "El precio unitario es obligatorio")
    private Double precioUnitario;
}
