package com.pdv.lacumbre.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no debe exceder 255 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 13, message = "El RFC no debe exceder 13 caracteres")
    @Column(name = "rfc", unique = true)
    private String rfc;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    @Column(name = "telefono")
    private String telefono;

    @Size(max = 100)
    @jakarta.validation.constraints.Email(message = "El formato del correo no es válido")
    @Column(name = "email", unique = true)
    private String email;


    @Size(max = 1000, message = "La dirección es demasiado larga")
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

}