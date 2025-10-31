package com.empresa.compras.mapper;

import com.empresa.compras.dtos.PagoDetalleRequestDTO;
import com.empresa.compras.dtos.PagoDetalleResponseDTO;
import com.empresa.compras.entity.MetodoPago;
import com.empresa.compras.entity.Pago;
import com.empresa.compras.entity.PagoDetalle;
import com.empresa.compras.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagoDetalleMapper {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    public MetodoPago toEntityMetodo(PagoDetalleRequestDTO dto) {
        return metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException(
                        "❌ Método de pago no encontrado con ID: " + dto.getIdMetodoPago()
                ));
    }

    public PagoDetalle toEntity(PagoDetalleRequestDTO dto, Pago pago, MetodoPago metodo) {
        PagoDetalle detalle = new PagoDetalle();
        detalle.setPago(pago);
        detalle.setMetodoPago(metodo);
        detalle.setMontoParcial(dto.getMontoParcial());
        return detalle;
    }

    public PagoDetalleResponseDTO toResponse(PagoDetalle detalle) {
        return new PagoDetalleResponseDTO(
                detalle.getIdDetalle(),
                detalle.getMetodoPago().getNombreMetodo(),
                detalle.getMontoParcial()
        );
    }
}
