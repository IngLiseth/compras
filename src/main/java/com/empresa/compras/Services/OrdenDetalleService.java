package com.empresa.compras.Services;

import com.empresa.compras.dtos.OrdenDetalleRequestDTO;
import com.empresa.compras.dtos.OrdenDetalleResponseDTO;
import com.empresa.compras.entity.Insumo;
import com.empresa.compras.entity.OrdenCompra;
import com.empresa.compras.entity.OrdenDetalle;
import com.empresa.compras.repository.InsumoRepository;
import com.empresa.compras.repository.OrdenCompraRepository;
import com.empresa.compras.repository.OrdenDetalleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenDetalleService {

    private final OrdenDetalleRepository ordenDetalleRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final InsumoRepository insumoRepository;

    public OrdenDetalleService(
            OrdenDetalleRepository ordenDetalleRepository,
            OrdenCompraRepository ordenCompraRepository,
            InsumoRepository insumoRepository) {
        this.ordenDetalleRepository = ordenDetalleRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.insumoRepository = insumoRepository;
    }

    // Crear detalle en una orden
    @Transactional
    public OrdenDetalleResponseDTO crearDetalle(Long idOrden, OrdenDetalleRequestDTO dto) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        Insumo insumo = insumoRepository.findById(dto.getIdInsumo())
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

        OrdenDetalle detalle = new OrdenDetalle();
        detalle.setOrden(orden);
        detalle.setInsumo(insumo);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());

        OrdenDetalle guardado = ordenDetalleRepository.save(detalle);

        return new OrdenDetalleResponseDTO(
                guardado.getId().getIdOrden(),
                guardado.getInsumo().getIdInsumo(),
                guardado.getInsumo().getNombreInsumo(),
                guardado.getCantidad(),
                guardado.getPrecioUnitario(),
                guardado.getSubtotal()
        );
    }

    // Listar detalles de una orden
    @Transactional
    public List<OrdenDetalleResponseDTO> listarDetallesPorOrden(Long idOrden) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        return orden.getDetalles().stream()
                .map(d -> new OrdenDetalleResponseDTO(
                        d.getId().getIdOrden(),
                        d.getInsumo().getIdInsumo(),
                        d.getInsumo().getNombreInsumo(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()
                ))
                .collect(Collectors.toList());
    }

    // Actualizar un detalle
    @Transactional
    public OrdenDetalleResponseDTO actualizarDetalle(Long idOrden, Long idDetalle, OrdenDetalleRequestDTO dto) {
        OrdenDetalle detalle = ordenDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));

        Insumo insumo = insumoRepository.findById(dto.getIdInsumo())
                .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

        detalle.setInsumo(insumo);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitario(dto.getPrecioUnitario());

        OrdenDetalle actualizado = ordenDetalleRepository.save(detalle);

        return new OrdenDetalleResponseDTO(
                actualizado.getId().getIdOrden(),
                actualizado.getInsumo().getIdInsumo(),
                actualizado.getInsumo().getNombreInsumo(),
                actualizado.getCantidad(),
                actualizado.getPrecioUnitario(),
                actualizado.getSubtotal()
        );
    }

    // Eliminar un detalle
    @Transactional
    public void eliminarDetalle(Long idOrden, Long idDetalle) {
        OrdenDetalle detalle = ordenDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));

        if (!detalle.getOrden().getIdOrden().equals(idOrden)) {
            throw new IllegalArgumentException("El detalle no pertenece a esta orden");
        }

        ordenDetalleRepository.delete(detalle);
    }
}
