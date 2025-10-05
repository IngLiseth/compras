package com.empresa.compras.mapper;

import com.empresa.compras.dtos.OrdenDetalleRequestDTO;
import com.empresa.compras.dtos.OrdenDetalleResponseDTO;
import com.empresa.compras.entity.Insumo;
import com.empresa.compras.entity.OrdenCompra;
import com.empresa.compras.entity.OrdenDetalle;
import com.empresa.compras.entity.OrdenDetalleId;
import org.springframework.stereotype.Component;

@Component
public class OrdenDetalleMapper {

    // Convertir de DTO a entidad
    public OrdenDetalle toEntity(OrdenDetalleRequestDTO dto, OrdenCompra orden, Insumo insumo) {
        OrdenDetalle detalle = new OrdenDetalle();
        detalle.setId(new OrdenDetalleId(orden.getIdOrden(), insumo.getIdInsumo()));
        detalle.setOrden(orden);
        detalle.setInsumo(insumo);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());
        return detalle;
    }

    // Convertir de entidad a DTO
    public OrdenDetalleResponseDTO toResponse(OrdenDetalle detalle) {
        return new OrdenDetalleResponseDTO(
                detalle.getOrden().getIdOrden(),
                detalle.getInsumo().getIdInsumo(),
                detalle.getInsumo().getNombreInsumo(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
