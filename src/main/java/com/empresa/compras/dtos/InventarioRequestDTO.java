package com.empresa.compras.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequestDTO {
    private Long idInsumo;
    private Integer stockActual;
}
