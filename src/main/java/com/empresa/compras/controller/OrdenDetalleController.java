package com.empresa.compras.controller;

import com.empresa.compras.Services.OrdenDetalleService;
import com.empresa.compras.dtos.OrdenDetalleRequestDTO;
import com.empresa.compras.dtos.OrdenDetalleResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes/{idOrden}/detalles")
public class OrdenDetalleController {

    private final OrdenDetalleService ordenDetalleService;

    public OrdenDetalleController(OrdenDetalleService ordenDetalleService) {
        this.ordenDetalleService = ordenDetalleService;
    }

    // Crear un detalle dentro de una orden
    @PostMapping
    public ResponseEntity<OrdenDetalleResponseDTO> crearDetalle(
            @PathVariable Long idOrden,
            @Valid @RequestBody OrdenDetalleRequestDTO dto) {
        return ResponseEntity.status(201).body(ordenDetalleService.crearDetalle(idOrden, dto));
    }

    // Listar detalles de una orden
    @GetMapping
    public ResponseEntity<List<OrdenDetalleResponseDTO>> listarDetalles(@PathVariable Long idOrden) {
        return ResponseEntity.ok(ordenDetalleService.listarDetallesPorOrden(idOrden));
    }

    // Actualizar un detalle específico
    @PutMapping("/{idDetalle}")
    public ResponseEntity<OrdenDetalleResponseDTO> actualizarDetalle(
            @PathVariable Long idOrden,
            @PathVariable Long idDetalle,
            @Valid @RequestBody OrdenDetalleRequestDTO dto) {
        return ResponseEntity.ok(ordenDetalleService.actualizarDetalle(idOrden, idDetalle, dto));
    }

    // Eliminar un detalle
    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<Void> eliminarDetalle(
            @PathVariable Long idOrden,
            @PathVariable Long idDetalle) {
        ordenDetalleService.eliminarDetalle(idOrden, idDetalle);
        return ResponseEntity.noContent().build();
    }
}
