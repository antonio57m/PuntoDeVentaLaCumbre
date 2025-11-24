package com.pdv.lacumbre.service.usuario;

import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.enums.Rol;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioEntity> obtenerTodosLosUsuarios();
    List<UsuarioEntity> obtenerUsuariosActivos();
    List<UsuarioEntity> buscarUsuarios(String termino); // El buscador por nombre
    List<UsuarioEntity> obtenerPorRol(Rol rol);
    Optional<UsuarioEntity> obtenerPorId(Long id);
    Optional<UsuarioEntity> obtenerPorUsername(String username);
    UsuarioEntity crearUsuario(UsuarioEntity usuario);
    UsuarioEntity actualizarUsuario(Long id, UsuarioEntity usuarioDetalles);
    void eliminarUsuario(Long id);
}