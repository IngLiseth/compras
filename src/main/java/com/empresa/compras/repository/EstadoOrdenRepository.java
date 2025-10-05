package com.empresa.compras.repository;

import com.empresa.compras.entity.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden, Long> {
    boolean existsByNombreEstadoIgnoreCase(String nombreEstado);
}
