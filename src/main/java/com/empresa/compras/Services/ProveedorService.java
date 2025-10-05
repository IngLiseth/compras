
package com.empresa.compras.Services;

import com.empresa.compras.dtos.ProveedorRequestDTO;
import com.empresa.compras.dtos.ProveedorResponseDTO;
import com.empresa.compras.entity.Proveedor;
import com.empresa.compras.mapper.ProveedorMapper;
import com.empresa.compras.repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        if (proveedorRepository.existsByNit(dto.getNit())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese NIT");
        }
        if (proveedorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un proveedor con ese email");
        }

        Proveedor proveedor = proveedorMapper.toEntity(dto);
        Proveedor guardado = proveedorRepository.save(proveedor);
        return proveedorMapper.toResponse(guardado);
    }

    @Transactional
    public List<ProveedorResponseDTO> listar() {
        return proveedorRepository.findAll().stream()
                .map(proveedorMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProveedorResponseDTO obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        return proveedorMapper.toResponse(proveedor);
    }

    @Transactional
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        proveedorMapper.updateEntity(proveedor, dto);
        Proveedor actualizado = proveedorRepository.save(proveedor);
        return proveedorMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new IllegalArgumentException("Proveedor no encontrado");
        }
        proveedorRepository.deleteById(id);
    }
}
