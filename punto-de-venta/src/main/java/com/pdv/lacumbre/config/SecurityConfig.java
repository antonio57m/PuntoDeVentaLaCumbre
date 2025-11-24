package com.pdv.lacumbre.config;

import com.pdv.lacumbre.security.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // <--- IMPORTANTE
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. RECURSOS ESTÁTICOS (CSS, JS, IMAGES)
                        // PathRequest.toStaticResources() libera automáticamente: /css/**, /js/**, /images/**, /webjars/**
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // Liberamos también /vendor por si usas librerías externas ahí
                        .requestMatchers("/vendor/**").permitAll()

                        // 2. PÁGINAS PÚBLICAS
                        .requestMatchers("/login").permitAll()

                        // 3. RUTAS PROTEGIDAS POR ROL
                        .requestMatchers("/usuarios/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ventas/**", "/productos/**", "/clientes/**", "/categorias/**").hasAnyRole("CAJERO", "ADMIN")

                        // 4. CUALQUIER OTRA COSA REQUIERE LOGIN
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true) // true fuerza ir al home tras login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}