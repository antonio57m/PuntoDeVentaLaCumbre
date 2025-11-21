package com.pdv.lacumbre.model.entity;

import com.pdv.lacumbre.model.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre no debe exceder 50 caracteres") // Ajustado a BD
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    // Nota: Esta validación revisa el largo del HASH, no del input original.
    @Column(name = "contrasena_hash", nullable = false)
    private String contrasena;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 150, message = "El nombre no debe exceder 150 caracteres") // Ajustado a BD
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @NotNull(message = "El estado es obligatorio")
    @Column(name = "activo", nullable = false)
    @Builder.Default // Vital para que funcione el = true con Lombok
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
}