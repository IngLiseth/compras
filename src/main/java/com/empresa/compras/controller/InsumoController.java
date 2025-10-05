package com.empresa.compras.controller;

import com.empresa.compras.dtos.InsumoRequestDTO;
import com.empresa.compras.dtos.InsumoResponseDTO;
import com.empresa.compras.Services.InsumoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @PostMapping
    public ResponseEntity<InsumoResponseDTO> crear(@Valid @RequestBody InsumoRequestDTO dto) {
        return ResponseEntity.status(201).body(insumoService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<InsumoResponseDTO>> listar() {
        return ResponseEntity.ok(insumoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(insumoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumoResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody InsumoRequestDTO dto) {
        return ResponseEntity.ok(insumoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        insumoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
