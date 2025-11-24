package com.pdv.lacumbre.service.venta;

import com.pdv.lacumbre.model.entity.DetalleVentaEntity;
import com.pdv.lacumbre.model.entity.VentaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VentaService {
    VentaEntity registrarVenta(VentaEntity venta, List<DetalleVentaEntity> detalles);
    void cancelarVenta(Long idVenta);
    Optional<VentaEntity> obtenerVentaPorId(Long id);
    List<VentaEntity> obtenerVentasDelDia();
    List<VentaEntity> obtenerHistorialPorCliente(Long idCliente);
    List<DetalleVentaEntity> obtenerDetallesDeVenta(Long idVenta);
    public List<VentaEntity> generarReportePorRango(LocalDate fechaInicio, LocalDate fechaFin);
}
