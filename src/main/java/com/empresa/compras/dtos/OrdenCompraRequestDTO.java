package com.empresa.compras.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompraRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Size(max = 255, message = "Las observaciones no deben superar 255 caracteres")
    private String observaciones;

    @NotNull(message = "Debe indicar el usuario que crea la orden")
    private Long idUsuario;

    @NotNull(message = "Debe indicar el proveedor")
    private Long idProveedor;

    @NotNull(message = "Debe indicar el estado de la orden")
    private Long idEstado;

    @NotNull(message = "Debe incluir al menos un detalle en la orden")
    private List<OrdenDetalleRequestDTO> detalles;
}
