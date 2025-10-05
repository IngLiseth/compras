package com.empresa.compras.Services;

import com.empresa.compras.dtos.RecepcionRequestDTO;
import com.empresa.compras.dtos.RecepcionResponseDTO;
import com.empresa.compras.dtos.RecepcionDetalleRequestDTO;
import com.empresa.compras.entity.*;
import com.empresa.compras.mapper.RecepcionMapper;
import com.empresa.compras.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecepcionService {

    private final RecepcionRepository recepcionRepository;
    private final RecepcionDetalleRepository recepcionDetalleRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final InsumoRepository insumoRepository;
    private final InventarioRepository inventarioRepository; // 🔹 nuevo
    private final RecepcionMapper recepcionMapper;

    public RecepcionService(
            RecepcionRepository recepcionRepository,
            RecepcionDetalleRepository recepcionDetalleRepository,
            OrdenCompraRepository ordenCompraRepository,
            InsumoRepository insumoRepository,
            InventarioRepository inventarioRepository, // 🔹 nuevo
            RecepcionMapper recepcionMapper
    ) {
        this.recepcionRepository = recepcionRepository;
        this.recepcionDetalleRepository = recepcionDetalleRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.insumoRepository = insumoRepository;
        this.inventarioRepository = inventarioRepository; // 🔹 nuevo
        this.recepcionMapper = recepcionMapper;
    }

    // ----------------- CREAR -----------------
    @Transactional
    public RecepcionResponseDTO crear(RecepcionRequestDTO dto) {
        // 1. Validar orden de compra
        OrdenCompra orden = ordenCompraRepository.findById(dto.getIdOrden())
                .orElseThrow(() -> new IllegalArgumentException("Orden de compra no encontrada"));

        // 2. Crear recepción
        Recepcion recepcion = new Recepcion();
        recepcion.setFecha(dto.getFecha());
        recepcion.setObservaciones(dto.getObservaciones());
        recepcion.setOrden(orden);

        Recepcion guardada = recepcionRepository.save(recepcion);

        // 3. Crear detalles y actualizar inventario
        List<RecepcionDetalle> detalles = new ArrayList<>();
        for (RecepcionDetalleRequestDTO detDto : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detDto.getIdInsumo())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

            RecepcionDetalle detalle = new RecepcionDetalle();

            // Crear ID compuesto
            RecepcionDetalleId detalleId = new RecepcionDetalleId(
                    guardada.getIdRecepcion(),
                    insumo.getIdInsumo()
            );
            detalle.setId(detalleId);
            detalle.setRecepcion(guardada);
            detalle.setInsumo(insumo);
            detalle.setCantidadRecibida(detDto.getCantidadRecibida());

            detalles.add(recepcionDetalleRepository.save(detalle));
            // ACTUALIZAR INVENTARIO
            Inventario inventario = inventarioRepository.findById(insumo.getIdInsumo())
                    .orElseGet(() -> {
                        Inventario nuevo = new Inventario();
                        nuevo.setInsumo(insumo);
                        nuevo.setStockActual(0);
                        return nuevo;
                    });

            // sumar la cantidad recibida
            inventario.setStockActual(inventario.getStockActual() + detDto.getCantidadRecibida());

            // guardar inventario actualizado
            inventarioRepository.save(inventario);
        }

        guardada.setDetalles(detalles);

        // 4. Retornar DTO con mapper
        return recepcionMapper.toResponse(guardada);
    }

    // ----------------- ACTUALIZAR -----------------
    @Transactional
    public RecepcionResponseDTO actualizar(Long id, RecepcionRequestDTO dto) {
        Recepcion recepcion = recepcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recepción no encontrada"));

        recepcion.setFecha(dto.getFecha());
        recepcion.setObservaciones(dto.getObservaciones());

        OrdenCompra orden = ordenCompraRepository.findById(dto.getIdOrden())
                .orElseThrow(() -> new IllegalArgumentException("Orden de compra no encontrada"));
        recepcion.setOrden(orden);

        // limpiar detalles previos
        recepcion.getDetalles().clear();

        List<RecepcionDetalle> detalles = new ArrayList<>();
        for (RecepcionDetalleRequestDTO detDto : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detDto.getIdInsumo())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo no encontrado"));

            RecepcionDetalle detalle = new RecepcionDetalle();
            detalle.setId(new RecepcionDetalleId(recepcion.getIdRecepcion(), insumo.getIdInsumo()));
            detalle.setRecepcion(recepcion);
            detalle.setInsumo(insumo);
            detalle.setCantidadRecibida(detDto.getCantidadRecibida());

            detalles.add(detalle);

            Inventario inventario = inventarioRepository.findById(insumo.getIdInsumo())
                    .orElseGet(() -> {
                        Inventario nuevo = new Inventario();
                        nuevo.setInsumo(insumo);
                        nuevo.setStockActual(0); // iniciar en 0
                        return nuevo;
                    });

            //  sumar inventario
            inventario.setStockActual(
                    inventario.getStockActual() + detDto.getCantidadRecibida()
            );

            inventarioRepository.save(inventario);
        }

        recepcion.setDetalles(detalles);

        Recepcion actualizada = recepcionRepository.save(recepcion);

        return recepcionMapper.toResponse(actualizada);
    }

    // ----------------- LISTAR -----------------
    @Transactional
    public List<RecepcionResponseDTO> listar() {
        return recepcionRepository.findAll()
                .stream()
                .map(recepcionMapper::toResponse)
                .toList();
    }

    // ----------------- OBTENER POR ID -----------------
    @Transactional
    public RecepcionResponseDTO obtenerPorId(Long id) {
        Recepcion recepcion = recepcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recepción no encontrada"));
        return recepcionMapper.toResponse(recepcion);
    }

    // ----------------- ELIMINAR -----------------
    @Transactional
    public void eliminar(Long id) {
        if (!recepcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Recepción no encontrada");
        }
        recepcionRepository.deleteById(id);
    }
}
