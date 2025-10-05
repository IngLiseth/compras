package com.empresa.compras.repository;

import com.empresa.compras.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);

    // Para validaciones al actualizar (ignora el id actual)
    boolean existsByEmailIgnoreCaseAndIdUsuarioNot(String email, Long idUsuario);
    boolean existsByUsernameIgnoreCaseAndIdUsuarioNot(String username, Long idUsuario);
}
