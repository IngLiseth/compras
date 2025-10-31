package com.empresa.compras.dtos;

import com.empresa.compras.dtos.PagoDetalleResponseDTO;
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
public class PagoResponseDTO {
    private Long idPago;
    private LocalDate fecha;
    private Double montoTotal;
    private Long idOrden; // opcional, depende de lo que quieras mostrar
    private List<PagoDetalleResponseDTO> detalles;
}