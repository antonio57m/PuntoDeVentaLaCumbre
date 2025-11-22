package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.DetalleVentaEntity;
import com.pdv.lacumbre.model.entity.ProductoEntity;
import com.pdv.lacumbre.model.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {
    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
    List<DetalleVentaEntity> findByProducto(ProductoEntity producto);
    Optional<DetalleVentaEntity> findByVentaAndProducto(VentaEntity venta, ProductoEntity producto);
    @Query("SELECT SUM(d.cantidad) FROM DetalleVentaEntity d WHERE d.producto = :producto")
    BigDecimal totalVendidoPorProducto(@Param("producto") ProductoEntity producto);
}