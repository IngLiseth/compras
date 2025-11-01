package com.empresa.compras.Services;

import com.empresa.compras.dtos.LoginRequestDTO;
import com.empresa.compras.dtos.LoginResponseDTO;
import com.empresa.compras.entity.Usuario;
import com.empresa.compras.repository.RolRepository;
import com.empresa.compras.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;


    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí deberías usar BCrypt, pero por simplicidad comparamos texto plano
        if (!usuario.getPasswordHash().equals(dto.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponseDTO(
                "Login exitoso",
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getRol().getNombreRol()
        );
    }
}
