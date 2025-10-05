package com.empresa.compras.controller;

import com.empresa.compras.dtos.PagoRequestDTO;
import com.empresa.compras.dtos.PagoResponseDTO;
import com.empresa.compras.Services.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    // Crear
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crear(@RequestBody PagoRequestDTO dto) {
        return ResponseEntity.ok(pagoService.crear(dto));
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> listar() {
        return ResponseEntity.ok(pagoService.listar());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PagoRequestDTO dto
    ) {
        return ResponseEntity.ok(pagoService.actualizar(id, dto));
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
