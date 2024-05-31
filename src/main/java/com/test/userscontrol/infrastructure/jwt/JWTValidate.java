package com.test.userscontrol.infrastructure.jwt;

import com.test.userscontrol.infrastructure.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.test.userscontrol.infrastructure.jwt.Constants.*;

public class JWTValidate {
    // Método para verificar si el token existe en la petición
    public static boolean tokenExists(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene el valor del header "Authorization" de la petición HTTP
        String header = request.getHeader(HEADER_AUTHORIZATION);
        // Verifica si el header es nulo o no empieza con el prefijo "Bearer "
        if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) return false;
        // Si el header existe y empieza con "Bearer ", retorna true
        return true;
    }

    // Método para validar el token JWT
    public static Claims JWTValid(HttpServletRequest request) {
        // Obtiene el valor del header "Authorization" y elimina el prefijo "Bearer "
        String jwtToken = request.getHeader(HEADER_AUTHORIZATION).replace(TOKEN_BEARER_PREFIX, "");
        // Valida y parsea el token JWT usando la clave secreta para obtener las Claims (reclamaciones)
        return Jwts.parser().setSigningKey(getSignedKey(SUPER_SECRET_KEY)).build().parseClaimsJws(jwtToken).getBody();
    }

    // Método para autenticar al usuario en el contexto de seguridad de Spring
    public static void setAuthetication(Claims claims, CustomUserDetailService customUserDetailService) {
        // Carga los detalles del usuario usando el nombre de usuario (subject) de las Claims
        UserDetails userDetails = customUserDetailService.loadUserByUsername(claims.getSubject());
        // Crea un objeto UsernamePasswordAuthenticationToken con los detalles del usuario y sus autoridades
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // Establece la autenticación en el contexto de seguridad de Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
