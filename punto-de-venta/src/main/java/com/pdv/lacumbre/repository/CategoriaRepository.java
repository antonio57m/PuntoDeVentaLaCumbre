package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // <--- Importante

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    Optional<CategoriaEntity> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<CategoriaEntity> findByNombreContainingIgnoreCase(String nombre);
}