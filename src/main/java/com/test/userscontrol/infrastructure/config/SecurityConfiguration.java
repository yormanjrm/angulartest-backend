package com.test.userscontrol.infrastructure.config;

import com.test.userscontrol.infrastructure.jwt.JWTAuthorizationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    // Constructor que inyecta la dependencia JWTAuthorizationFilter.
    public SecurityConfiguration(JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    // Define un bean para el AuthenticationManager.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuración del filtro de seguridad HTTP.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Desactiva la protección CSRF.
        httpSecurity.csrf(csrf -> csrf.disable())
                // Configura las autorizaciones de las solicitudes HTTP.
                .authorizeHttpRequests(
                        aut -> aut
                                // Requiere el rol ADMIN para acceder a la ruta "/api/users/register".
                                .requestMatchers("/api/users/register").hasRole("ADMIN")
                                // Requiere el rol ADMIN para acceder a la ruta "/api/users/delete".
                                .requestMatchers("/api/users/delete").hasRole("ADMIN")
                                // Requiere el rol ADMIN para acceder a la ruta "/api/users/get/byId".
                                .requestMatchers("/api/users/get/byId").hasRole("ADMIN")
                                // Requiere autenticación para acceder a la ruta "/api/users/get/all".
                                .requestMatchers("/api/users/get/all").authenticated()
                                // Permite acceso sin autenticación a la ruta "/api/login".
                                .requestMatchers("/api/login").permitAll())
                // Agrega el filtro JWTAuthorizationFilter después del filtro UsernamePasswordAuthenticationFilter.
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // Configura el manejo de excepciones para la autenticación.
                .exceptionHandling(exceptionHandling ->
                        // Establece el código de estado HTTP a 401 (Unauthorized) si ocurre una excepción de autenticación.
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                );
        // Construye y devuelve el objeto SecurityFilterChain.
        return httpSecurity.build();
    }

    // Define un bean para el codificador de contraseñas BCrypt.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}