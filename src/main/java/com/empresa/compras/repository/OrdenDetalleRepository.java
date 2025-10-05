package com.empresa.compras.repository;

import com.empresa.compras.entity.OrdenDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenDetalleRepository extends JpaRepository<OrdenDetalle, Long> {
}
