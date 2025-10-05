package com.empresa.compras.mapper;

import com.empresa.compras.dtos.InventarioRequestDTO;
import com.empresa.compras.dtos.InventarioResponseDTO;
import com.empresa.compras.entity.Insumo;
import com.empresa.compras.entity.Inventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public Inventario toEntity(InventarioRequestDTO dto, Insumo insumo) {
        Inventario inventario = new Inventario();
        inventario.setInsumo(insumo);
        inventario.setStockActual(dto.getStockActual());
        return inventario;
    }

    public InventarioResponseDTO toResponse(Inventario inventario) {
        return new InventarioResponseDTO(
                inventario.getIdInsumo(),
                inventario.getInsumo().getNombreInsumo(),
                inventario.getStockActual()
        );
    }
}
