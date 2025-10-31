package com.empresa.compras.repository;

import com.empresa.compras.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}