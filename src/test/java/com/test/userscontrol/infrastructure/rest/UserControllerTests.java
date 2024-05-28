package com.test.userscontrol.infrastructure.rest;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.rest.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {
        // Crear un usuario de prueba
        User user = new User(null, "John Doe", "john.doe@example.com", "password", "user", "image.jpg", null, null);

        // Configurar el comportamiento simulado del servicio de usuario
        when(userService.save(user)).thenReturn(user);

        // Llamar al método save del controlador de usuario
        ResponseEntity<User> responseEntity = userController.save(user);

        // Verificar que el servicio de usuario fue llamado con el usuario correcto
        verify(userService).save(user);

        // Verificar que la respuesta contiene el usuario correcto y el estado HTTP 201 (CREATED)
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }
}