package com.empresa.compras.Services;

import com.empresa.compras.dtos.EstadoOrdenRequestDTO;
import com.empresa.compras.dtos.EstadoOrdenResponseDTO;
import com.empresa.compras.entity.EstadoOrden;
import com.empresa.compras.mapper.EstadoOrdenMapper;
import com.empresa.compras.repository.EstadoOrdenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoOrdenService {

    private final EstadoOrdenRepository estadoOrdenRepository;
    private final EstadoOrdenMapper estadoOrdenMapper;

    public EstadoOrdenService(EstadoOrdenRepository estadoOrdenRepository, EstadoOrdenMapper estadoOrdenMapper) {
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.estadoOrdenMapper = estadoOrdenMapper;
    }

    @Transactional
    public EstadoOrdenResponseDTO crear(EstadoOrdenRequestDTO dto) {
        if (estadoOrdenRepository.existsByNombreEstadoIgnoreCase(dto.getNombreEstado())) {
            throw new IllegalArgumentException("Ya existe un estado con ese nombre");
        }
        EstadoOrden estado = estadoOrdenMapper.toEntity(dto);
        EstadoOrden guardado = estadoOrdenRepository.save(estado);
        return estadoOrdenMapper.toResponse(guardado);
    }

    @Transactional
    public List<EstadoOrdenResponseDTO> listar() {
        return estadoOrdenRepository.findAll()
                .stream()
                .map(estadoOrdenMapper::toResponse)
                .toList();
    }

    @Transactional
    public EstadoOrdenResponseDTO obtenerPorId(Long id) {
        EstadoOrden estado = estadoOrdenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));
        return estadoOrdenMapper.toResponse(estado);
    }

    @Transactional
    public EstadoOrdenResponseDTO actualizar(Long id, EstadoOrdenRequestDTO dto) {
        EstadoOrden estado = estadoOrdenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));
        estadoOrdenMapper.updateEntity(estado, dto);
        EstadoOrden actualizado = estadoOrdenRepository.save(estado);
        return estadoOrdenMapper.toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!estadoOrdenRepository.existsById(id)) {
            throw new IllegalArgumentException("Estado no encontrado");
        }
        estadoOrdenRepository.deleteById(id);
    }
}
