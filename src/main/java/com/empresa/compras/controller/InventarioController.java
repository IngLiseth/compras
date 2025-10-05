package com.empresa.compras.controller;

import com.empresa.compras.Services.InventarioService;
import com.empresa.compras.dtos.InventarioResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Listar todos los inventarios
    @GetMapping
    public List<InventarioResponseDTO> listar() {
        return inventarioService.listar();
    }

    // Buscar inventario por id de insumo
    @GetMapping("/{idInsumo}")
    public InventarioResponseDTO buscar(@PathVariable Long idInsumo) {
        return inventarioService.buscar(idInsumo);
    }
}
