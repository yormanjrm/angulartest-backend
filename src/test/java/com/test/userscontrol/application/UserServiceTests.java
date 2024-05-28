package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUser() {
        // Crear un usuario de prueba
        User user = new User(null, "John Doe", "john.doe@example.com", "password", "ADMIN", "default.png", null, null);

        // Configurar el comportamiento simulado del userRepository.save
        when(userRepository.save(user)).thenReturn(user);

        // Llamar al método save del UserService
        User savedUser = userService.save(user);

        // Verificar que el método save del userRepository se llamó exactamente una vez
        verify(userRepository, times(1)).save(user);

        // Verificar que el usuario devuelto es el mismo que el usuario guardado originalmente
        assertEquals(user, savedUser);
    }
}