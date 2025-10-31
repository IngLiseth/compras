package com.empresa.compras.controller;

import com.empresa.compras.dtos.PagoDetalleRequestDTO;
import com.empresa.compras.dtos.PagoDetalleResponseDTO;
import com.empresa.compras.Services.PagoDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos-detalles")
@RequiredArgsConstructor
public class PagoDetalleController {

    private final PagoDetalleService pagoDetalleService;

    // Crear un detalle asociado a un Pago
    @PostMapping("/pago/{idPago}")
    public ResponseEntity<PagoDetalleResponseDTO> crear(
            @PathVariable Long idPago,
            @RequestBody PagoDetalleRequestDTO dto) {

        return ResponseEntity.ok(pagoDetalleService.crear(idPago, dto));
    }

    // Listar detalles de un pago
    @GetMapping("/pago/{idPago}")
    public ResponseEntity<List<PagoDetalleResponseDTO>> listarPorPago(@PathVariable Long idPago) {
        return ResponseEntity.ok(pagoDetalleService.listarPorPago(idPago));
    }

    // Buscar detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoDetalleResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoDetalleService.buscarPorId(id));
    }
    @PutMapping("/detalle/{idDetalle}")
    public ResponseEntity<String> actualizarDetalle(
            @PathVariable Long idDetalle,
            @RequestParam Double montoParcial,
            @RequestParam Long idMetodoPago) {

        pagoDetalleService.actualizarDetalleYRecalcular(idDetalle, montoParcial, idMetodoPago);
        return ResponseEntity.ok("Detalle actualizado ✅");
    }



    // Actualizar detalle
    @PutMapping("/{id}")
    public ResponseEntity<PagoDetalleResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PagoDetalleRequestDTO dto) {

        return ResponseEntity.ok(pagoDetalleService.actualizar(id, dto));
    }

    // Eliminar detalle
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoDetalleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}