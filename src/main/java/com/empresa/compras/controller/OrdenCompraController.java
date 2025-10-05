package com.empresa.compras.controller;

import com.empresa.compras.Services.OrdenCompraService;
import com.empresa.compras.dtos.OrdenCompraRequestDTO;
import com.empresa.compras.dtos.OrdenCompraResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    // Crear orden con detalles
    @PostMapping
    public ResponseEntity<OrdenCompraResponseDTO> crear(
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        return ResponseEntity.status(201).body(ordenCompraService.crear(dto));
    }

    // Listar todas las órdenes
    @GetMapping
    public ResponseEntity<List<OrdenCompraResponseDTO>> listar() {
        return ResponseEntity.ok(ordenCompraService.listar());
    }

    // Obtener una orden por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenCompraService.obtenerPorId(id));
    }

    // Actualizar orden (incluye detalles)
    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdenCompraRequestDTO dto) {
        return ResponseEntity.ok(ordenCompraService.actualizar(id, dto));
    }

    // Eliminar orden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ordenCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
