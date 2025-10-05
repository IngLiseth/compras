package com.empresa.compras.controller;

import com.empresa.compras.Services.MetodoPagoService;
import com.empresa.compras.dtos.MetodoPagoRequestDTO;
import com.empresa.compras.dtos.MetodoPagoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    // Crear
    @PostMapping
    public ResponseEntity<MetodoPagoResponseDTO> crear(@RequestBody MetodoPagoRequestDTO dto) {
        return ResponseEntity.ok(metodoPagoService.crear(dto));
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<MetodoPagoResponseDTO>> listar() {
        return ResponseEntity.ok(metodoPagoService.listar());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(metodoPagoService.buscarPorId(id));
    }

    // 🔹 Actualizar (ESTE ES EL QUE TE FALTA)
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody MetodoPagoRequestDTO dto) {
        return ResponseEntity.ok(metodoPagoService.actualizar(id, dto));
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        metodoPagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
