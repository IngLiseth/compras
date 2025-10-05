package com.empresa.compras.mapper;

import com.empresa.compras.dtos.EstadoOrdenRequestDTO;
import com.empresa.compras.dtos.EstadoOrdenResponseDTO;
import com.empresa.compras.entity.EstadoOrden;
import org.springframework.stereotype.Component;

    @Component
    public class EstadoOrdenMapper {

    public EstadoOrden toEntity(EstadoOrdenRequestDTO dto) {
        EstadoOrden estado = new EstadoOrden();
        estado.setNombreEstado(dto.getNombreEstado());
        return estado;
    }

    public void updateEntity(EstadoOrden estado, EstadoOrdenRequestDTO dto) {
        estado.setNombreEstado(dto.getNombreEstado());
    }

    public EstadoOrdenResponseDTO toResponse(EstadoOrden estado) {
        return new EstadoOrdenResponseDTO(
                estado.getIdEstado(),
                estado.getNombreEstado()
        );
    }
}
