package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.ClienteEntity;
import com.pdv.lacumbre.model.entity.PagoCreditoEntity;
import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.enums.MetodoPago; // Importante
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoCreditoRepository extends JpaRepository<PagoCreditoEntity, Long> {
    List<PagoCreditoEntity> findByClienteOrderByFechaDesc(ClienteEntity cliente);
    List<PagoCreditoEntity> findByUsuario(UsuarioEntity usuario);
    List<PagoCreditoEntity> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<PagoCreditoEntity> findByFechaBetweenAndMetodoPagoAbono(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            MetodoPago metodoPago
    );
    @Query("SELECT SUM(p.monto) FROM PagoCreditoEntity p WHERE p.cliente = :cliente")
    BigDecimal sumarTotalAbonadoPorCliente(@Param("cliente") ClienteEntity cliente);
}