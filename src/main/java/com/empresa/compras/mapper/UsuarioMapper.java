package com.empresa.compras.mapper;

import com.empresa.compras.dtos.UsuarioRequestDTO;
import com.empresa.compras.dtos.UsuarioResponseDTO;
import com.empresa.compras.entity.Rol;
import com.empresa.compras.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setPasswordHash(dto.getPasswordHash());
        // Si no mandan activo en el request, por defecto queda true
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        usuario.setRol(rol);
        return usuario;
    }

    public void updateEntity(Usuario usuario, UsuarioRequestDTO dto, Rol rol) {
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setPasswordHash(dto.getPasswordHash());
        // Manejo seguro de null en activo
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : usuario.getActivo());
        usuario.setRol(rol);
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getActivo(),
                usuario.getRol().getNombreRol()
        );
    }
}