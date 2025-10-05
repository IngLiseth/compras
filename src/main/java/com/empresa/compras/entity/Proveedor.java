package com.empresa.compras.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "proveedores")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    @Column(nullable = false, length = 100)
    private String nombreProveedor;

    @Column(length = 150)
    private String direccion;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Relación con teléfonos
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelefonoProveedor> telefonos;
}