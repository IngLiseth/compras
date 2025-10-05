package com.empresa.compras.Services;

import com.empresa.compras.dtos.PagoDetalleRequestDTO;
import com.empresa.compras.dtos.PagoDetalleResponseDTO;
import com.empresa.compras.entity.MetodoPago;
import com.empresa.compras.entity.Pago;
import com.empresa.compras.entity.PagoDetalle;
import com.empresa.compras.mapper.PagoDetalleMapper;
import com.empresa.compras.repository.MetodoPagoRepository;
import com.empresa.compras.repository.PagoDetalleRepository;
import com.empresa.compras.repository.PagoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleService {

    private final PagoDetalleRepository detalleRepository;
    private final PagoRepository pagoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PagoDetalleMapper detalleMapper;

    // Crear un detalle de pago
    @Transactional
    public PagoDetalleResponseDTO crear(Long idPago, PagoDetalleRequestDTO dto) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("❌ Pago no encontrado con id " + idPago));

        MetodoPago metodo = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("❌ Método de pago no encontrado con id " + dto.getIdMetodoPago()));

        PagoDetalle detalle = new PagoDetalle();
        detalle.setPago(pago);
        detalle.setMetodoPago(metodo);
        detalle.setMontoParcial(dto.getMontoParcial());

        PagoDetalle guardado = detalleRepository.save(detalle);
        return detalleMapper.toResponse(guardado);
    }

    // Listar todos los detalles de un pago
    @Transactional
    public List<PagoDetalleResponseDTO> listarPorPago(Long idPago) {
        return detalleRepository.findByPago_IdPago(idPago).stream()
                .map(detalleMapper::toResponse)
                .toList();
    }

    // Buscar un detalle por su ID
    @Transactional
    public PagoDetalleResponseDTO buscarPorId(Long id) {
        PagoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Detalle no encontrado con id " + id));
        return detalleMapper.toResponse(detalle);
    }

    // Actualizar un detalle
    @Transactional
    public PagoDetalleResponseDTO actualizar(Long id, PagoDetalleRequestDTO dto) {
        PagoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Detalle no encontrado con id " + id));

        MetodoPago metodo = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("❌ Método de pago no encontrado con id " + dto.getIdMetodoPago()));

        detalle.setMetodoPago(metodo);
        detalle.setMontoParcial(dto.getMontoParcial());

        return detalleMapper.toResponse(detalleRepository.save(detalle));
    }

    // Eliminar un detalle
    @Transactional
    public void eliminar(Long id) {
        if (!detalleRepository.existsById(id)) {
            throw new RuntimeException("❌ No existe detalle con id " + id);
        }
        detalleRepository.deleteById(id);
    }
}
