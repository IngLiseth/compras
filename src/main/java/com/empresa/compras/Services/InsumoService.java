package com.empresa.compras.Services;

import com.empresa.compras.dtos.InsumoRequestDTO;
import com.empresa.compras.dtos.InsumoResponseDTO;
import com.empresa.compras.entity.Insumo;
import com.empresa.compras.mapper.InsumoMapper;
import com.empresa.compras.repository.InsumoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;
    private final InsumoMapper insumoMapper;

    public InsumoService(InsumoRepository insumoRepository, InsumoMapper insumoMapper) {
        this.insumoRepository = insumoRepository;
        this.insumoMapper = insumoMapper;
    }

    @Transactional
    public InsumoResponseDTO crear(InsumoRequestDTO dto) {
        if (insumoRepository.existsByNombreInsumoIgnoreCase(dto.getNombreInsumo())) {
            throw new IllegalArgumentException("Ya existe un insumo con ese nombre");
        }
        Insumo insumo = insumoMapper.toEntity(dto);
        Insumo guardado = insumoRepository.save(insumo);
        return insumoMapper.toResponse(guardado);
    }

    @Transactional
    public List<InsumoResponseDTO> listar() {
        return insumoRepository.findAll()
                .stream()
                .map(insumoMapper::toResponse)
                .toList();
    }

    @Transactional
    public InsumoResponseDTO obtenerPorId(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));
        return insumoMapper.toResponse(insumo);
    }

    @Transactional
    public InsumoResponseDTO actualizar(Long id, InsumoRequestDTO dto) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));
        insumoMapper.updateEntity(insumo, dto);
        Insumo actualizado = insumoRepository.save(insumo);
        return insumoMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!insumoRepository.existsById(id)) {
            throw new IllegalArgumentException("Insumo no encontrado");
        }
        insumoRepository.deleteById(id);
    }
}
