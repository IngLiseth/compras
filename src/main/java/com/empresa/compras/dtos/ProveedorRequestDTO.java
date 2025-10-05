
package com.empresa.compras.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorRequestDTO {

    @NotBlank
    private String nit;

    @NotBlank
    private String nombreProveedor;

    private String direccion;

    @Email
    @NotBlank
    private String email;

    private List<String> telefonos; // solo números
}
