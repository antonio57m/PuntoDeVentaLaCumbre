package com.pdv.lacumbre.model.dto; // Sugerencia de paquete

import com.pdv.lacumbre.model.enums.MetodoPago;
import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private Long idCliente; // Puede ser null
    private MetodoPago metodoPago;
    private List<ProductoVentaDTO> productos;
}