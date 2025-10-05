package com.empresa.compras.mapper;

import com.empresa.compras.dtos.InsumoRequestDTO;
import com.empresa.compras.dtos.InsumoResponseDTO;
import com.empresa.compras.entity.Insumo;
import org.springframework.stereotype.Component;

@Component
public class InsumoMapper {

    public Insumo toEntity(InsumoRequestDTO dto) {
        Insumo insumo = new Insumo();
        insumo.setNombreInsumo(dto.getNombreInsumo());
        insumo.setDescripcion(dto.getDescripcion());
        insumo.setUnidadMedida(dto.getUnidadMedida());
        insumo.setPrecioUnitario(dto.getPrecioUnitario());
        return insumo;
    }

    public void updateEntity(Insumo insumo, InsumoRequestDTO dto) {
        insumo.setNombreInsumo(dto.getNombreInsumo());
        insumo.setDescripcion(dto.getDescripcion());
        insumo.setUnidadMedida(dto.getUnidadMedida());
        insumo.setPrecioUnitario(dto.getPrecioUnitario());
    }

    public InsumoResponseDTO toResponse(Insumo insumo) {
        return new InsumoResponseDTO(
                insumo.getIdInsumo(),
                insumo.getNombreInsumo(),
                insumo.getDescripcion(),
                insumo.getUnidadMedida(),
                insumo.getPrecioUnitario()
        );
    }
}
