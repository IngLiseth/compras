// ProveedorMapper.java
package com.empresa.compras.mapper;

import com.empresa.compras.dtos.ProveedorRequestDTO;
import com.empresa.compras.dtos.ProveedorResponseDTO;
import com.empresa.compras.entity.Proveedor;
import com.empresa.compras.entity.TelefonoProveedor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProveedorMapper {

    public Proveedor toEntity(ProveedorRequestDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNit(dto.getNit());
        proveedor.setNombreProveedor(dto.getNombreProveedor());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());

        if (dto.getTelefonos() != null) {
            List<TelefonoProveedor> telefonos = dto.getTelefonos().stream()
                    .map(num -> new TelefonoProveedor(null, num, proveedor))
                    .collect(Collectors.toList());
            proveedor.setTelefonos(telefonos);
        }

        return proveedor;
    }

    public ProveedorResponseDTO toResponse(Proveedor proveedor) {
        List<String> telefonos = proveedor.getTelefonos() != null
                ? proveedor.getTelefonos().stream()
                .map(TelefonoProveedor::getNumero)
                .collect(Collectors.toList())
                : null;

        return new ProveedorResponseDTO(
                proveedor.getIdProveedor(),
                proveedor.getNit(),
                proveedor.getNombreProveedor(),
                proveedor.getDireccion(),
                proveedor.getEmail(),
                telefonos
        );
    }

    public void updateEntity(Proveedor proveedor, ProveedorRequestDTO dto) {
        proveedor.setNit(dto.getNit());
        proveedor.setNombreProveedor(dto.getNombreProveedor());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setEmail(dto.getEmail());

        if (dto.getTelefonos() != null) {
            List<TelefonoProveedor> telefonos = dto.getTelefonos().stream()
                    .map(num -> new TelefonoProveedor(null, num, proveedor))
                    .collect(Collectors.toList());
            proveedor.getTelefonos().clear();
            proveedor.getTelefonos().addAll(telefonos);
        }
    }
}
