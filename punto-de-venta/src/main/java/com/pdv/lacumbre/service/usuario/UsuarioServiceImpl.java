package com.pdv.lacumbre.service.usuario;

import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.model.enums.Rol;
import com.pdv.lacumbre.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> obtenerPorUsername(String username) {
        return usuarioRepository.findByNombreUsuario(username);
    }
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> obtenerPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEntity> buscarUsuarios(String termino) {
        return usuarioRepository.findByNombreCompletoContainingIgnoreCaseOrNombreUsuarioContainingIgnoreCase(termino, termino);
    }
    @Override
    @Transactional
    public UsuarioEntity crearUsuario(UsuarioEntity usuario) {
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe: " + usuario.getNombreUsuario());
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setContrasena(usuario.getContrasena());
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
    @Override
    @Transactional
    public UsuarioEntity actualizarUsuario(Long id, UsuarioEntity usuarioDetalles) {
        UsuarioEntity usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        if (!usuarioExistente.getNombreUsuario().equals(usuarioDetalles.getNombreUsuario())
                && usuarioRepository.existsByNombreUsuario(usuarioDetalles.getNombreUsuario())) {
            throw new RuntimeException("El nuevo nombre de usuario ya está ocupado");
        }
        usuarioExistente.setNombreUsuario(usuarioDetalles.getNombreUsuario());
        usuarioExistente.setNombreCompleto(usuarioDetalles.getNombreCompleto());
        usuarioExistente.setRol(usuarioDetalles.getRol());
        if (usuarioDetalles.getContrasena() != null && !usuarioDetalles.getContrasena().isEmpty()) {
            usuarioExistente.setContrasena(passwordEncoder.encode(usuarioDetalles.getContrasena()));
        }
        return usuarioRepository.save(usuarioExistente);
    }
    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
