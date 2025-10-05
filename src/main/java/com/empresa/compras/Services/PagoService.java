package com.empresa.compras.Services;
import com.empresa.compras.dtos.PagoRequestDTO;
import com.empresa.compras.dtos.PagoResponseDTO;
import com.empresa.compras.entity.OrdenCompra;
import com.empresa.compras.entity.Pago;
import com.empresa.compras.mapper.PagoMapper;
import com.empresa.compras.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.empresa.compras.repository.OrdenCompraRepository;
@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final OrdenCompraRepository ordenCompraRepository; // 👈 Necesitas este repo

    // Crear
    @Transactional
    public PagoResponseDTO crear(PagoRequestDTO dto) {
        // Buscar la orden a la que pertenece el pago
        OrdenCompra orden = ordenCompraRepository.findById(dto.getIdOrden())
                .orElseThrow(() -> new RuntimeException("❌ Orden no encontrada con ID " + dto.getIdOrden()));

        // Mapear el DTO a entidad usando el mapper
        Pago pago = pagoMapper.toEntity(dto, orden);

        // Guardar y devolver respuesta
        return pagoMapper.toResponse(pagoRepository.save(pago));
    }

    // Listar todos
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listar() {
        return pagoRepository.findAll()
                .stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public PagoResponseDTO buscarPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Pago no encontrado con ID " + id));
        return pagoMapper.toResponse(pago);
    }

    // Actualizar
    @Transactional
    public PagoResponseDTO actualizar(Long id, PagoRequestDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Pago no encontrado con ID " + id));

        OrdenCompra orden = ordenCompraRepository.findById(dto.getIdOrden())
                .orElseThrow(() -> new RuntimeException("❌ Orden no encontrada con ID " + dto.getIdOrden()));

        pago.setFecha(dto.getFecha());
        pago.setMontoTotal(dto.getMontoTotal());
        pago.setOrden(orden);

        return pagoMapper.toResponse(pagoRepository.save(pago));
    }

    // Eliminar
    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("❌ No existe pago con ID " + id);
        }
        pagoRepository.deleteById(id);
    }
}
