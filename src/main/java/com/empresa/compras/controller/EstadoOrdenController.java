package com.empresa.compras.controller;

import com.empresa.compras.dtos.EstadoOrdenRequestDTO;
import com.empresa.compras.dtos.EstadoOrdenResponseDTO;
import com.empresa.compras.Services.EstadoOrdenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-orden")
public class EstadoOrdenController {

    private final EstadoOrdenService estadoOrdenService;

    public EstadoOrdenController(EstadoOrdenService estadoOrdenService) {
        this.estadoOrdenService = estadoOrdenService;
    }

    @PostMapping
    public ResponseEntity<EstadoOrdenResponseDTO> crear(@Valid @RequestBody EstadoOrdenRequestDTO dto) {
        return ResponseEntity.status(201).body(estadoOrdenService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstadoOrdenResponseDTO>> listar() {
        return ResponseEntity.ok(estadoOrdenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoOrdenResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estadoOrdenService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoOrdenResponseDTO> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody EstadoOrdenRequestDTO dto) {
        return ResponseEntity.ok(estadoOrdenService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadoOrdenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
