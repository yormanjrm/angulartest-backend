package com.test.userscontrol.infrastructure.jwt;

import com.test.userscontrol.infrastructure.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.test.userscontrol.infrastructure.jwt.JWTValidate.*;

@Component // Anotación para indicar que esta clase es un componente gestionado por Spring.
public class JWTAuthorizationFilter extends OncePerRequestFilter { // Extiende OncePerRequestFilter para asegurar que el filtro se ejecuta una vez por solicitud.

    private CustomUserDetailService customUserDetailService; // Declaración de una variable final para el servicio de detalles de usuario personalizado.

    // Constructor que inyecta el servicio de detalles de usuario personalizado.
    public JWTAuthorizationFilter(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    // Sobrescribe el método doFilterInternal de OncePerRequestFilter para definir el comportamiento del filtro.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Verifica si el token existe en la solicitud.
            if (tokenExists(request, response)) {
                // Si el token existe, valida el JWT y obtiene los claims.
                Claims claims = JWTValid(request);
                // Verifica si los claims contienen autoridades.
                if (claims.get("authorities") != null) {
                    // Si existen autoridades, establece la autenticación en el contexto de seguridad.
                    setAuthetication(claims, customUserDetailService);
                } else {
                    // Si no existen autoridades, limpia el contexto de seguridad.
                    SecurityContextHolder.clearContext();
                }
            } else {
                // Si no existe un token, limpia el contexto de seguridad.
                SecurityContextHolder.clearContext();
            }
            // Continúa con el siguiente filtro en la cadena.
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            // Si se lanza alguna excepción relacionada con el JWT, se configura la respuesta como no autorizada.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }
}