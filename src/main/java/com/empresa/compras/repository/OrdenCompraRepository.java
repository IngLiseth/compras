package com.empresa.compras.repository;

import com.empresa.compras.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
}
