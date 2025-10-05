package com.empresa.compras.Services;

import com.empresa.compras.dtos.MetodoPagoRequestDTO;
import com.empresa.compras.dtos.MetodoPagoResponseDTO;
import com.empresa.compras.entity.MetodoPago;
import com.empresa.compras.mapper.MetodoPagoMapper;
import com.empresa.compras.repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;
    private final MetodoPagoMapper metodoPagoMapper;

    // Crear
    @Transactional
    public MetodoPagoResponseDTO crear(MetodoPagoRequestDTO dto) {
        MetodoPago metodo = metodoPagoMapper.toEntity(dto);
        return metodoPagoMapper.toResponse(metodoPagoRepository.save(metodo));
    }

    // Listar todos
    @Transactional(readOnly = true)
    public List<MetodoPagoResponseDTO> listar() {
        return metodoPagoRepository.findAll()
                .stream()
                .map(metodoPagoMapper::toResponse)
                .toList();
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public MetodoPagoResponseDTO buscarPorId(Long id) {
        MetodoPago metodo = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        return metodoPagoMapper.toResponse(metodo);
    }

    // Actualizar
    @Transactional
    public MetodoPagoResponseDTO actualizar(Long id, MetodoPagoRequestDTO dto) {
        MetodoPago metodo = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        metodo.setNombreMetodo(dto.getNombreMetodo());

        return metodoPagoMapper.toResponse(metodoPagoRepository.save(metodo));
    }

    // Eliminar
    @Transactional
    public void eliminar(Long id) {
        if (!metodoPagoRepository.existsById(id)) {
            throw new RuntimeException("Método de pago no encontrado");
        }
        metodoPagoRepository.deleteById(id);
    }
}
