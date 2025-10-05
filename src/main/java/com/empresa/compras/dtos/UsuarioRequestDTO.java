package com.empresa.compras.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email
    private String email;

    @NotBlank(message = "El username es obligatorio")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String passwordHash;

    private Boolean activo;

    private Long idRol; // para asignar un rol ya existente
}
