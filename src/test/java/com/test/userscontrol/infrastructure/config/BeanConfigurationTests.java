package com.test.userscontrol.infrastructure.config;

import com.test.userscontrol.application.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// Indica que esta clase es una prueba de Spring Boot y carga la configuración de la aplicación
@SpringBootTest
public class BeanConfigurationTests {

    // Inyecta el bean UserService para poder realizar pruebas en él
    @Autowired
    private UserService userService;

    // Este método de prueba verifica si el contexto de la aplicación se carga correctamente
    @Test
    void contextLoads() {
        // Verifica que el bean UserService se haya inicializado correctamente y no sea nulo
        assertNotNull(userService);
    }
}
