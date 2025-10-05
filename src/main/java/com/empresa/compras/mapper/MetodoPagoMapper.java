package com.empresa.compras.mapper;

import com.empresa.compras.dtos.MetodoPagoRequestDTO;
import com.empresa.compras.dtos.MetodoPagoResponseDTO;
import com.empresa.compras.entity.MetodoPago;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoMapper {

    public MetodoPago toEntity(MetodoPagoRequestDTO dto) {
        MetodoPago metodo = new MetodoPago();
        metodo.setNombreMetodo(dto.getNombreMetodo());
        return metodo;
    }

    public MetodoPagoResponseDTO toResponse(MetodoPago metodo) {
        return new MetodoPagoResponseDTO(
                metodo.getIdMetodoPago(),
                metodo.getNombreMetodo()
        );
    }
}
