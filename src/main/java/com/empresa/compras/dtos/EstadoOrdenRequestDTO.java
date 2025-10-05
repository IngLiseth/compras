package com.empresa.compras.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoOrdenRequestDTO {
    @NotBlank(message = "El nombre del estado es obligatorio")
    private String nombreEstado;
}
