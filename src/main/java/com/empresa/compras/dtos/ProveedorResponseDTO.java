// ProveedorResponseDTO.java
package com.empresa.compras.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponseDTO {

    private Long idProveedor;
    private String nit;
    private String nombreProveedor;
    private String direccion;
    private String email;
    private List<String> telefonos;
}
