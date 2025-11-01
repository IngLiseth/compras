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
    private final RolRepository rolRepository; // ✅ agregar

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }
    @PostConstruct
    public void initAdmin() {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@test.com");
            admin.setUsername("admin");
            admin.setPasswordHash("123"); // si usas BCrypt me dices y lo pongo
            admin.setActivo(true);
            admin.setRol(rolRepository.findById(1L).orElse(null)); // rol admin = 1

            usuarioRepository.save(admin);
            System.out.println("✅ Usuario admin creado automáticamente");
        }
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
