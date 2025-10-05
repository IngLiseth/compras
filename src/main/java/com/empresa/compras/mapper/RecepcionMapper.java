package com.empresa.compras.mapper;

import com.empresa.compras.dtos.*;
import com.empresa.compras.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecepcionMapper {

    // Convierte de Entity -> ResponseDTO
    public RecepcionResponseDTO toResponse(Recepcion recepcion) {
        return new RecepcionResponseDTO(
                recepcion.getIdRecepcion(),
                recepcion.getFecha(),
                recepcion.getObservaciones(),
                recepcion.getOrden().getIdOrden(), // referencia a la orden
                recepcion.getDetalles().stream()
                        .map(this::toDetalleResponse)
                        .collect(Collectors.toList())
        );
    }

    // Convierte cada detalle
    private RecepcionDetalleResponseDTO toDetalleResponse(RecepcionDetalle detalle) {
        return new RecepcionDetalleResponseDTO(
                detalle.getInsumo().getIdInsumo(),
                detalle.getInsumo().getNombreInsumo(),
                detalle.getCantidadRecibida()
        );
    }
}
