package com.jose.microservicio_productos_crud_reactivo;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public MapReactiveUserDetailsService users() throws Exception{
        List<UserDetails> users = List.of(
            User.withUsername("user1").password("{noop}user1").roles("USERS").build(),
            User.withUsername("administrador").password("{noop}administrador").roles("USERS", "ADMIN").build(),
            User.withUsername("user2").password("{noop}user2").roles("USERS").build()
        );
        return new MapReactiveUserDetailsService(users);
    }

    @Bean
    public SecurityWebFilterChain filter(ServerHttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(c -> c.disable()).authorizeExchange(auth -> auth
        .pathMatchers(HttpMethod.POST, "/api/productos/alta").hasAnyRole("ADMIN")
        .pathMatchers(HttpMethod.DELETE, "/api/productos/eliminar/**").hasAnyRole("ADMIN", "USERS")
        .pathMatchers(HttpMethod.GET, "/api/productos/buscarPorCodigo/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
        .pathMatchers(HttpMethod.PUT, "/api/productos/actualizar/**").permitAll()
        .pathMatchers("/api/productos/**").authenticated()
        .anyExchange().permitAll())
        .httpBasic(Customizer.withDefaults());


        return httpSecurity.build();
    }
}
