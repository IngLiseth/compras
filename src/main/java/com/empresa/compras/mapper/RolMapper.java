package com.empresa.compras.mapper;

import com.empresa.compras.dtos.RolRequestDTO;
import com.empresa.compras.dtos.RolResponseDTO;
import com.empresa.compras.entity.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol toEntity(RolRequestDTO dto) {
        Rol rol = new Rol();
        rol.setNombreRol(dto.getNombreRol());
        return rol;
    }

    public void updateEntity(Rol rol, RolRequestDTO dto) {
        rol.setNombreRol(dto.getNombreRol());
    }

    public RolResponseDTO toResponse(Rol rol) {
        return new RolResponseDTO(rol.getIdRol(), rol.getNombreRol());
    }
}
