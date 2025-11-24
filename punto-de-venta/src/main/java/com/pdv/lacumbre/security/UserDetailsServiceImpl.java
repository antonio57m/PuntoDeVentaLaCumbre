package com.pdv.lacumbre.security;

import com.pdv.lacumbre.model.entity.UsuarioEntity;
import com.pdv.lacumbre.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
        return new User(
                usuario.getNombreUsuario(),
                usuario.getContrasena(),
                usuario.getActivo(),
                true,
                true,
                true,
                Collections.singletonList(authority)
        );
    }
}