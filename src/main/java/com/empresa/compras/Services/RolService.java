package com.empresa.compras.Services;

import com.empresa.compras.dtos.RolRequestDTO;
import com.empresa.compras.dtos.RolResponseDTO;
import com.empresa.compras.entity.Rol;
import com.empresa.compras.mapper.RolMapper;
import com.empresa.compras.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {
    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public RolService(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    //  Crear rol
    @Transactional
    public RolResponseDTO crear(RolRequestDTO dto) {
        Rol entity = rolMapper.toEntity(dto);
        Rol guardado = rolRepository.save(entity);
        return rolMapper.toResponse(guardado);
    }

    // Listar todos los roles
    @Transactional
    public List<RolResponseDTO> listar() {
        return rolRepository.findAll()
                .stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    //  Obtener rol por id
    @Transactional
    public RolResponseDTO obtenerPorId(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));
        return rolMapper.toResponse(rol);
    }

    // Actualizar rol
    @Transactional
    public RolResponseDTO actualizar(Long id, RolRequestDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));
        rolMapper.updateEntity(rol, dto);
        Rol actualizado = rolRepository.save(rol);
        return rolMapper.toResponse(actualizado);
    }

    //  Eliminar rol
    @Transactional
    public void eliminar(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new IllegalArgumentException("Rol no encontrado con id: " + id);
        }
        rolRepository.deleteById(id);
    }
}

