package com.test.userscontrol.infrastructure.jwt;

import io.jsonwebtoken.security.Keys; // Importa la clase Keys de la biblioteca io.jsonwebtoken.security para trabajar con claves de seguridad.

import java.nio.charset.StandardCharsets; // Importa la clase StandardCharsets de java.nio.charset para especificar conjuntos de caracteres estándar.
import java.security.Key; // Importa la clase Key de java.security para representar claves de seguridad.

public class Constants { // Declara una clase pública llamada Constants.

    // Define una constante pública y estática llamada HEADER_AUTHORIZATION que almacena el nombre del encabezado de autorización.
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // Define una constante pública y estática llamada TOKEN_BEARER_PREFIX que almacena el prefijo de los tokens Bearer.
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // Define una constante pública y estática llamada SUPER_SECRET_KEY que almacena una clave secreta utilizada para firmar tokens JWT.
    public static final String SUPER_SECRET_KEY = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmF";

    // Define una constante pública y estática llamada TOKEN_EXPIRATION_TIME que almacena el tiempo de expiración de los tokens en milisegundos.
    public static final long TOKEN_EXPIRATION_TIME = 2000000;

    // Declara un método público y estático llamado getSignedKey que toma un parámetro de tipo String llamado secretKey y devuelve un objeto de tipo Key.
    public static Key getSignedKey(String secretKey) {
        // Convierte la clave secreta en un array de bytes utilizando la codificación UTF-8.
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // Genera y devuelve una clave HMAC SHA utilizando el array de bytes de la clave secreta.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
