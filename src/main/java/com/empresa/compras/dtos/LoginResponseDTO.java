package com.empresa.compras.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String mensaje;
    private Long idUsuario;
    private String username;
    private String rol;
}
