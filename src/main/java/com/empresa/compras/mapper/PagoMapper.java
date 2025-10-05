package com.empresa.compras.mapper;

import com.empresa.compras.dtos.PagoRequestDTO;
import com.empresa.compras.dtos.PagoResponseDTO;
import com.empresa.compras.entity.OrdenCompra;
import com.empresa.compras.entity.Pago;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagoMapper {

    private final PagoDetalleMapper detalleMapper;

    public PagoMapper(PagoDetalleMapper detalleMapper) {
        this.detalleMapper = detalleMapper;
    }

    public Pago toEntity(PagoRequestDTO dto, OrdenCompra orden) {
        Pago pago = new Pago();
        pago.setFecha(dto.getFecha());
        pago.setMontoTotal(dto.getMontoTotal());
        pago.setOrden(orden);
        // aseguramos que la lista de detalles nunca sea null
        pago.setDetalles(List.of());
        return pago;
    }

    public PagoResponseDTO toResponse(Pago pago) {
        return new PagoResponseDTO(
                pago.getIdPago(),
                pago.getFecha(),
                pago.getMontoTotal(),
                pago.getOrden().getIdOrden(), // solo devolvemos el id de la orden
                (pago.getDetalles() != null
                        ? pago.getDetalles().stream()
                        .map(detalleMapper::toResponse)
                        .toList()
                        : List.of())
        );
    }
}
