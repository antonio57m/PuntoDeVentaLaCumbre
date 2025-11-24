package com.pdv.lacumbre.model.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoVentaDTO {
    private Long idProducto;
    private BigDecimal cantidad;
    private BigDecimal precio; // Precio al momento de la venta
}