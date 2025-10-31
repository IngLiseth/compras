package com.empresa.compras.repository;

import com.empresa.compras.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    MetodoPago findByNombreMetodoIgnoreCase(String nombreMetodo);



}
