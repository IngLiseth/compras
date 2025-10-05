package com.empresa.compras.controller;

import com.empresa.compras.Services.RolService;
import com.empresa.compras.dtos.RolRequestDTO;
import com.empresa.compras.dtos.RolResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // Crear rol
    @PostMapping
    public ResponseEntity<RolResponseDTO> crear(@RequestBody RolRequestDTO dto) {
        RolResponseDTO nuevoRol = rolService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRol);
    }

    //  Listar todos los roles
    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> listar() {
        return ResponseEntity.ok(rolService.listar());
    }

    //  Buscar un rol por id
    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> obtenerPorId(@PathVariable Long id) {
        RolResponseDTO rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(rol);
    }

    //  Actualizar un rol
    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> actualizar(@PathVariable Long id, @RequestBody RolRequestDTO dto) {
        RolResponseDTO rolActualizado = rolService.actualizar(id, dto);
        return ResponseEntity.ok(rolActualizado);
    }

    //  Eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
