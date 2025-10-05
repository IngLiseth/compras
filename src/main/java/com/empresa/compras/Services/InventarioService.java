package com.empresa.compras.Services;

import com.empresa.compras.dtos.InventarioRequestDTO;
import com.empresa.compras.dtos.InventarioResponseDTO;
import com.empresa.compras.entity.Insumo;
import com.empresa.compras.entity.Inventario;
import com.empresa.compras.mapper.InventarioMapper;
import com.empresa.compras.repository.InsumoRepository;
import com.empresa.compras.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final InsumoRepository insumoRepository;

    @Transactional
    public InventarioResponseDTO crear(InventarioRequestDTO dto) {
        Insumo insumo = insumoRepository.findById(dto.getIdInsumo())
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado"));

        Inventario inventario = inventarioMapper.toEntity(dto, insumo);
        return inventarioMapper.toResponse(inventarioRepository.save(inventario));
    }

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> listar() {
        return inventarioRepository.findAll().stream()
                .map(inventarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventarioResponseDTO buscar(Long idInsumo) {
        Inventario inventario = inventarioRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        return inventarioMapper.toResponse(inventario);
    }

    @Transactional
    public InventarioResponseDTO actualizar(Long idInsumo, InventarioRequestDTO dto) {
        Inventario inventario = inventarioRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventario.setStockActual(dto.getStockActual());
        return inventarioMapper.toResponse(inventarioRepository.save(inventario));
    }

    @Transactional
    public void eliminar(Long idInsumo) {
        if (!inventarioRepository.existsById(idInsumo)) {
            throw new RuntimeException("Inventario no encontrado");
        }
        inventarioRepository.deleteById(idInsumo);
    }
}

