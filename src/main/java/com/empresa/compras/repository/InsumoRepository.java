package com.empresa.compras.repository;

import com.empresa.compras.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    boolean existsByNombreInsumoIgnoreCase(String nombreInsumo);
}
