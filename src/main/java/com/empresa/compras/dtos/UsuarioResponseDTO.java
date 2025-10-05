package com.empresa.compras.dtos;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String username;
    private Boolean activo;
    private String nombreRol; // mostramos el rol como texto
}

