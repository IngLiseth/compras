package com.empresa.compras.mapper;

import com.empresa.compras.dtos.PagoRequestDTO;
import com.empresa.compras.dtos.PagoResponseDTO;
import com.empresa.compras.entity.OrdenCompra;
import com.empresa.compras.entity.Pago;
import com.empresa.compras.entity.PagoDetalle;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


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

        // ✅ Convertir los detalles
        List<PagoDetalle> detalles = dto.getDetalles()
                .stream()
                .map(det -> {
                    PagoDetalle d = new PagoDetalle();
                    d.setMontoParcial(det.getMontoParcial()); // ✅ toma el campo del DTO
                    d.setMetodoPago(detalleMapper.toEntityMetodo(det)); // ✅ buscar método
                    d.setPago(pago); // 🔥 relación bidireccional
                    return d;
                })
                .collect(Collectors.toList());

        pago.setDetalles(detalles);
        return pago;



    }
    public PagoResponseDTO toResponse(Pago pago) {
        return new PagoResponseDTO(
                pago.getIdPago(),
                pago.getFecha(),
                pago.getMontoTotal(),
                pago.getOrden().getIdOrden(),
                pago.getDetalles() != null
                        ? pago.getDetalles()
                        .stream()
                        .map(detalleMapper::toResponse)
                        .toList()
                        : List.of()
        );
    }

}
