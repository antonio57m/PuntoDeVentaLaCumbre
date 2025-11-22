package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    List<ClienteEntity> findByNombreContainingIgnoreCaseOrRfcContainingIgnoreCaseOrTelefonoContainingIgnoreCase(
            String nombre, String rfc, String telefono
    );
    List<ClienteEntity> findBySaldoActualGreaterThan(BigDecimal monto);
    boolean existsByRfc(String rfc);
    boolean existsByEmail(String email);
    Optional<ClienteEntity> findByRfc(String rfc);
}