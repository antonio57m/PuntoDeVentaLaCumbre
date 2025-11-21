package com.pdv.lacumbre.model.entity;

import com.pdv.lacumbre.model.enums.UnidadMedida;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
// 3. AQUÍ SE DEFINEN LOS ÍNDICES (INDEX sku, INDEX nombre)
@Table(name = "Productos", indexes = {
        @Index(name = "idx_sku", columnList = "sku"),
        @Index(name = "idx_nombre", columnList = "nombre")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50, message = "El SKU no debe exceder 50 caracteres")
    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Size(max = 50)
    @Column(name = "codigo_proveedor", unique = true)
    private String codigoProveedor;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no debe exceder 255 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "El precio de compra es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal precioCompra = BigDecimal.ZERO;

    @NotNull(message = "El precio de venta es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false, precision = 10, scale = 3)
    @Builder.Default
    private BigDecimal stock = BigDecimal.ZERO;

    @NotNull(message = "La unidad de medida es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false)
    private UnidadMedida unidadMedida;

    @PositiveOrZero(message = "El stock minimo no puede ser negativo")
    @Column(name = "stock_minimo", precision = 10, scale = 3)
    @Builder.Default
    private BigDecimal stockMinimo = BigDecimal.valueOf(5.000);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @NotNull
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
}