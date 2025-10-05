package com.empresa.compras.Services;

import com.empresa.compras.dtos.UsuarioRequestDTO;
import com.empresa.compras.dtos.UsuarioResponseDTO;
import com.empresa.compras.entity.Rol;
import com.empresa.compras.entity.Usuario;
import com.empresa.compras.mapper.UsuarioMapper;
import com.empresa.compras.repository.RolRepository;
import com.empresa.compras.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        if (usuarioRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese username");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        Usuario entity = usuarioMapper.toEntity(dto, rol);
        Usuario guardado = usuarioRepository.save(entity);
        return usuarioMapper.toResponse(guardado);
    }


    @Transactional
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Transactional
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar email único (ignora el usuario actual)
        if (usuarioRepository.existsByEmailIgnoreCaseAndIdUsuarioNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        // Validar username único (ignora el usuario actual)
        if (usuarioRepository.existsByUsernameIgnoreCaseAndIdUsuarioNot(dto.getUsername(), id)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese username");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        usuarioMapper.updateEntity(usuario, dto, rol);
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(actualizado);
    }



    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
