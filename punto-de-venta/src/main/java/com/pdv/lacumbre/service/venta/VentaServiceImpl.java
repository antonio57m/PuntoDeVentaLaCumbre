package com.pdv.lacumbre.service.venta;


import com.pdv.lacumbre.model.entity.*;
import com.pdv.lacumbre.model.enums.EstadoVenta;
import com.pdv.lacumbre.model.enums.MetodoPago;
import com.pdv.lacumbre.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public VentaEntity registrarVenta(VentaEntity venta, List<DetalleVentaEntity> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new RuntimeException("No se puede registrar una venta sin productos.");
        }
        BigDecimal totalCalculado = BigDecimal.ZERO;
        for (DetalleVentaEntity detalle : detalles) {
            ProductoEntity producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: ID " + detalle.getProducto().getId()));
            if (producto.getStock().compareTo(detalle.getCantidad()) < 0) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock().subtract(detalle.getCantidad()));
            productoRepository.save(producto); // Actualizamos el producto
            detalle.setProducto(producto);
            BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitarioVenta());
            totalCalculado = totalCalculado.add(subtotal);
        }
        venta.setTotal(totalCalculado);
        venta.setEstadoVenta(EstadoVenta.PAGADA);
        venta.setFecha(LocalDateTime.now());
        if (venta.getMetodoPago() == MetodoPago.CREDITO) {
            if (venta.getCliente() == null) {
                throw new RuntimeException("Para ventas a crédito, el cliente es obligatorio.");
            }
            ClienteEntity cliente = clienteRepository.findById(venta.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            BigDecimal nuevaDeuda = cliente.getSaldoActual().add(totalCalculado);
            if (cliente.getLimiteCredito().compareTo(BigDecimal.ZERO) > 0 &&
                    nuevaDeuda.compareTo(cliente.getLimiteCredito()) > 0) {
                throw new RuntimeException("La venta excede el límite de crédito del cliente.");
            }
            cliente.setSaldoActual(nuevaDeuda);
            clienteRepository.save(cliente);
            venta.setEstadoVenta(EstadoVenta.PENDIENTE);
        }
        VentaEntity ventaGuardada = ventaRepository.save(venta);
        for (DetalleVentaEntity detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaRepository.save(detalle);
        }
        return ventaGuardada;
    }
    @Override
    @Transactional
    public void cancelarVenta(Long idVenta) {
        VentaEntity venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA) {
            throw new RuntimeException("La venta ya está cancelada.");
        }
        List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVenta(venta);
        for (DetalleVentaEntity detalle : detalles) {
            ProductoEntity producto = detalle.getProducto();
            producto.setStock(producto.getStock().add(detalle.getCantidad()));
            productoRepository.save(producto);
        }
        if (venta.getMetodoPago() == MetodoPago.CREDITO && venta.getCliente() != null) {
            ClienteEntity cliente = venta.getCliente();
            BigDecimal nuevoSaldo = cliente.getSaldoActual().subtract(venta.getTotal());
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) nuevoSaldo = BigDecimal.ZERO;
            cliente.setSaldoActual(nuevoSaldo);
            clienteRepository.save(cliente);
        }
        venta.setEstadoVenta(EstadoVenta.CANCELADA);
        ventaRepository.save(venta);
    }
    @Override
    @Transactional(readOnly = true)
    public List<VentaEntity> generarReportePorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);
        return ventaRepository.findByFechaBetween(inicio, fin);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<VentaEntity> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<VentaEntity> obtenerVentasDelDia() {
        LocalDateTime inicio = LocalDate.now().atTime(LocalTime.MIN);
        LocalDateTime fin = LocalDate.now().atTime(LocalTime.MAX);
        return ventaRepository.findByFechaBetween(inicio, fin);
    }
    @Override
    @Transactional(readOnly = true)
    public List<VentaEntity> obtenerHistorialPorCliente(Long idCliente) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(idCliente);
        return ventaRepository.findByClienteOrderByFechaDesc(cliente);
    }
    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaEntity> obtenerDetallesDeVenta(Long idVenta) {
        VentaEntity venta = new VentaEntity();
        venta.setId(idVenta);
        return detalleVentaRepository.findByVenta(venta);
    }
}
