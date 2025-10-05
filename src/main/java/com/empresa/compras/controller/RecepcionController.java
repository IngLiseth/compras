package com.empresa.compras.controller;

import com.empresa.compras.Services.RecepcionService;
import com.empresa.compras.dtos.RecepcionRequestDTO;
import com.empresa.compras.dtos.RecepcionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionController {

    private final RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @PostMapping
    public ResponseEntity<RecepcionResponseDTO> crear(@Valid @RequestBody RecepcionRequestDTO dto) {
        return ResponseEntity.status(201).body(recepcionService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<RecepcionResponseDTO>> listar() {
        return ResponseEntity.ok(recepcionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecepcionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recepcionService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecepcionResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecepcionRequestDTO dto
    ) {
        return ResponseEntity.ok(recepcionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recepcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
