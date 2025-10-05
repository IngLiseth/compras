// ProveedorRepository.java
package com.empresa.compras.repository;

import com.empresa.compras.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNit(String nit);
    boolean existsByEmail(String email);
}
