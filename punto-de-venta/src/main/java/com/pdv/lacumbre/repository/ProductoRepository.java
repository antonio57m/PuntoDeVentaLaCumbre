package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.ProductoEntity;
import com.pdv.lacumbre.model.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    Optional<ProductoEntity> findBySkuOrCodigoProveedor(String sku, String codigoProveedor);
    List<ProductoEntity> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    boolean existsBySku(String sku);
    boolean existsByCodigoProveedor(String codigoProveedor);
    List<ProductoEntity> findByCategoriaAndActivoTrue(CategoriaEntity categoria);
    @Query("SELECT p FROM ProductoEntity p WHERE p.stock <= p.stockMinimo AND p.activo = true")
    List<ProductoEntity> encontrarProductosConBajoStock();
    Optional<ProductoEntity> findBySku(String sku);
}