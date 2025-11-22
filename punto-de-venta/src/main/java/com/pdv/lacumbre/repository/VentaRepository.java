package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.ClienteEntity;
import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.entity.VentaEntity;
import com.pdv.lacumbre.model.enums.EstadoVenta;
import com.pdv.lacumbre.model.enums.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
    List<VentaEntity> findByCliente(ClienteEntity cliente);
    List<VentaEntity> findByUsuario(UsuarioEntity usuario);
    List<VentaEntity> findByMetodoPago(MetodoPago metodoPago);
    List<VentaEntity> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<VentaEntity> findByEstadoVenta(EstadoVenta estadoVenta);
    List<VentaEntity> findByFechaBetweenAndEstadoVenta(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoVenta estadoVenta
    );
    List<VentaEntity> findByUsuarioOrderByFechaDesc(UsuarioEntity usuario);
}
