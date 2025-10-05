package com.empresa.compras.Services;

import com.empresa.compras.dtos.OrdenCompraRequestDTO;
import com.empresa.compras.dtos.OrdenCompraResponseDTO;
import com.empresa.compras.dtos.OrdenDetalleRequestDTO;
import com.empresa.compras.entity.*;
import com.empresa.compras.mapper.OrdenMapper;
import com.empresa.compras.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenDetalleRepository ordenDetalleRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final EstadoOrdenRepository estadoOrdenRepository;
    private final InsumoRepository insumoRepository;
    private final OrdenMapper ordenMapper;

    public OrdenCompraService(
            OrdenCompraRepository ordenCompraRepository,
            OrdenDetalleRepository ordenDetalleRepository,
            UsuarioRepository usuarioRepository,
            ProveedorRepository proveedorRepository,
            EstadoOrdenRepository estadoOrdenRepository,
            InsumoRepository insumoRepository,
            OrdenMapper ordenMapper
    ) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.ordenDetalleRepository = ordenDetalleRepository;
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.insumoRepository = insumoRepository;
        this.ordenMapper = ordenMapper;
    }

    // ----------------- CREAR -----------------
    @Transactional
    public OrdenCompraResponseDTO crear(OrdenCompraRequestDTO dto) {
        // 1. Validar usuario
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 2. Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        // 3. Validar estado
        EstadoOrden estado = estadoOrdenRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new IllegalArgumentException("Estado de orden no encontrado"));

        // 4. Crear la orden
        OrdenCompra orden = new OrdenCompra();
        orden.setFecha(dto.getFecha());
        orden.setObservaciones(dto.getObservaciones());
        orden.setUsuario(usuario);
        orden.setProveedor(proveedor);
        orden.setEstado(estado);

        OrdenCompra guardada = ordenCompraRepository.save(orden);

        // 5. Crear detalles con ID compuesto
        List<OrdenDetalle> detalles = new ArrayList<>();
        for (OrdenDetalleRequestDTO detDto : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detDto.getIdInsumo())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

            OrdenDetalle detalle = new OrdenDetalle();

            // Crear id compuesto (orden + insumo)
            OrdenDetalleId detalleId = new OrdenDetalleId(guardada.getIdOrden(), insumo.getIdInsumo());
            detalle.setId(detalleId);

            detalle.setOrden(guardada);
            detalle.setInsumo(insumo);
            detalle.setCantidad(detDto.getCantidad());
            detalle.setPrecioUnitario(detDto.getPrecioUnitario());

            detalles.add(ordenDetalleRepository.save(detalle));
        }

        // 6. Retornar DTO con mapper
        guardada.setDetalles(detalles);
        return ordenMapper.toResponse(guardada);
    }

    // ----------------- LISTAR -----------------
    @Transactional
    public List<OrdenCompraResponseDTO> listar() {
        return ordenCompraRepository.findAll()
                .stream()
                .map(ordenMapper::toResponse)
                .toList();
    }

    // ----------------- OBTENER POR ID -----------------
    @Transactional
    public OrdenCompraResponseDTO obtenerPorId(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        return ordenMapper.toResponse(orden);
    }

    // ----------------- ACTUALIZAR -----------------
    @Transactional
    public OrdenCompraResponseDTO actualizar(Long id, OrdenCompraRequestDTO dto) {

        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        orden.setFecha(dto.getFecha());
        orden.setObservaciones(dto.getObservaciones());

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
        EstadoOrden estado = estadoOrdenRepository.findById(dto.getIdEstado())
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado"));

        orden.setUsuario(usuario);
        orden.setProveedor(proveedor);
        orden.setEstado(estado);

        // 🔹 Limpia los detalles anteriores
        orden.getDetalles().clear();

// 🔹 Agrega los nuevos con ID compuesto
        for (OrdenDetalleRequestDTO detDto : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detDto.getIdInsumo())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

            OrdenDetalle detalle = new OrdenDetalle();

            // Crear clave compuesta (orden + insumo)
            OrdenDetalleId detalleId = new OrdenDetalleId(orden.getIdOrden(), insumo.getIdInsumo());
            detalle.setId(detalleId);

            detalle.setOrden(orden);
            detalle.setInsumo(insumo);
            detalle.setCantidad(detDto.getCantidad());
            detalle.setPrecioUnitario(detDto.getPrecioUnitario());

            orden.getDetalles().add(detalle);
        }

// 🔹 Guardar cambios
        OrdenCompra actualizada = ordenCompraRepository.save(orden);
        return ordenMapper.toResponse(actualizada);
    }



        // ----------------- ELIMINAR -----------------
    @Transactional
    public void eliminar(Long id) {
        if (!ordenCompraRepository.existsById(id)) {
            throw new IllegalArgumentException("Orden no encontrada");
        }
        ordenCompraRepository.deleteById(id);
    }
}
