package com.empresa.compras.dtos;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCompraResponseDTO {
    private Long idOrden;
    private String usuario;
    private String proveedor;
    private String estado;
    private String observaciones;
    private List<OrdenDetalleResponseDTO> detalles;
    private Double total;  // suma de subtotales
}
