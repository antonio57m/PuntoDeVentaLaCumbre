package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.ClienteEntity;
import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.entity.VentaEntity;
import com.pdv.lacumbre.model.enums.EstadoVenta;
import com.pdv.lacumbre.model.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
    List<VentaEntity> findByClienteOrderByFechaDesc(ClienteEntity cliente);
    List<VentaEntity> findByUsuarioOrderByFechaDesc(UsuarioEntity usuario);
    List<VentaEntity> findByMetodoPago(MetodoPago metodoPago);
    List<VentaEntity> findByEstadoVenta(EstadoVenta estadoVenta);
    List<VentaEntity> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<VentaEntity> findByFechaBetweenAndEstadoVenta(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoVenta estadoVenta
    );
    List<VentaEntity> findByFechaBetweenAndEstadoVentaAndMetodoPago(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoVenta estadoVenta,
            MetodoPago metodoPago
    );
    @Query("SELECT SUM(v.total) FROM VentaEntity v WHERE v.fecha BETWEEN :inicio AND :fin AND v.estadoVenta = :estado")
    BigDecimal sumarTotalVentasPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estado") EstadoVenta estado
    );
}
