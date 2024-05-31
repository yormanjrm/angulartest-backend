package com.test.userscontrol.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.test.userscontrol.infrastructure.jwt.Constants.*;
@Slf4j
@Service // Marca esta clase como un servicio gestionado por Spring.
public class JWTGenerator {
    // Método para generar un token JWT basado en el nombre de usuario.
    public String getToken(String username) {
        // Obtiene la lista de autoridades (permisos) del contexto de seguridad actual.
        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );
        log.info("authorityList :{}", authorityList);
        log.info("authorityList claiM :{}", authorityList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        // Construye el token JWT utilizando Jwts.builder().
        String token = Jwts.builder()
                .setId("userctrl") // Establece un ID para el JWT.
                .setSubject(username) // Establece el sujeto (nombre de usuario) del JWT.
                .claim("authorities", authorityList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) // Establece la fecha de expiración del token.
                .signWith(getSignedKey(SUPER_SECRET_KEY), SignatureAlgorithm.HS512).compact(); // Firma el token utilizando la clave secreta y el algoritmo HS512.
        return "Bearer " + token; // Devuelve el token prefijado con "Bearer ".
    }
}