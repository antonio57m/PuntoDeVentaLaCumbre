package com.pdv.lacumbre.repository;

import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByNombreUsuario(String nombreUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    List<UsuarioEntity> findByActivoTrue();
    List<UsuarioEntity> findByRol(Rol rol);
    List<UsuarioEntity> findByNombreCompletoContainingIgnoreCaseOrNombreUsuarioContainingIgnoreCase(String nombre, String usuario);
}