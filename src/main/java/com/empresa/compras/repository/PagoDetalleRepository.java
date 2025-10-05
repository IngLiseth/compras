package com.empresa.compras.repository;

import com.empresa.compras.entity.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Long> {

    // buscar detalles por id del pago
    List<PagoDetalle> findByPago_IdPago(Long idPago);
}
