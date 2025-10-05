package com.empresa.compras.repository;

import com.empresa.compras.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository <Rol, Long> {
    boolean existsByNombreRolIgnoreCase(String nombreRol);
}