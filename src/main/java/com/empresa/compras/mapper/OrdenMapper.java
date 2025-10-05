package com.empresa.compras.mapper;

import com.empresa.compras.dtos.*;
import com.empresa.compras.entity.*;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class OrdenMapper {

    public OrdenCompraResponseDTO toResponse(OrdenCompra orden) {
        Double total = orden.getDetalles().stream()
                .mapToDouble(OrdenDetalle::getSubtotal)
                .sum();

        return new OrdenCompraResponseDTO(
                orden.getIdOrden(),
                orden.getUsuario().getNombre(),
                orden.getProveedor().getNombreProveedor(),
                orden.getEstado().getNombreEstado(),
                orden.getObservaciones(),
                orden.getDetalles().stream()
                        .map(d -> new OrdenDetalleResponseDTO(
                                d.getOrden().getIdOrden(),
                                d.getInsumo().getIdInsumo(),
                                d.getInsumo().getNombreInsumo(),
                                d.getCantidad(),
                                d.getPrecioUnitario(),
                                d.getSubtotal()
                        )).collect(Collectors.toList()),
                total
        );
    }
}
