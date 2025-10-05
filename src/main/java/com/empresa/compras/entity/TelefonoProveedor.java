package com.empresa.compras.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "telefono_proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTelefono;

    @Column(nullable = false, length = 15)
    private String numero;

    // Relación con proveedor
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;
}