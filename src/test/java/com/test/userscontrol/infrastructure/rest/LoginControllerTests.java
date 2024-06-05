package com.test.userscontrol.infrastructure.rest;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import com.test.userscontrol.infrastructure.jwt.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private JWTGenerator jwtGenerator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private LoginController loginController;

    private User user;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks anotados en esta prueba, como los campos @Mock, @Spy, etc.
        MockitoAnnotations.openMocks(this);
        // Configura el MockMvc con el controlador 'loginController', creando un entorno de prueba standalone.
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        // Crea un nuevo objeto 'User' con valores de prueba.
        user = new User(1, "John Doe", "john.doe@example.com",
                "$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe",
                "ADMIN", "default.png", null);
    }

    @Test
    void testLogin_Success() throws Exception {
        // Simula que el método 'findByEmail' del 'userService' devuelve el objeto 'user' cuando se le pasa el email "john.doe@example.com".
        when(userService.findByEmail("john.doe@example.com")).thenReturn(user);
        // Simula que el método 'matches' del 'bCryptPasswordEncoder' devuelve true cuando se le pasa el password "password123" y el hash de contraseña almacenado.
        when(bCryptPasswordEncoder.matches("password123", "$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe")).thenReturn(true);
        // Simula que el método 'getToken' del 'jwtGenerator' devuelve el token "anytoken" cuando se le pasa el email "john.doe@example.com".
        when(jwtGenerator.getToken("john.doe@example.com")).thenReturn("anytoken");
        // Crea un objeto de tipo 'Authentication' simulado.
        Authentication authentication = mock(Authentication.class);
        // Simula que el método 'authenticate' del 'authenticationManager' devuelve el objeto 'authentication' cuando se le pasa cualquier 'UsernamePasswordAuthenticationToken'.
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        // Realiza una solicitud POST a la URL "/api/login" con los parámetros 'email' y 'password'.
        mockMvc.perform(post("/api/login")
                        .param("email", "john.doe@example.com")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON))
                // Verifica que el estado de la respuesta sea 200 (OK).
                .andExpect(status().isOk())
                // Verifica que el código en la respuesta JSON sea 200.
                .andExpect(jsonPath("$.code").value(200))
                // Verifica que el mensaje en la respuesta JSON sea "User authenticated".
                .andExpect(jsonPath("$.message").value("User authenticated"))
                // Verifica que el campo 'id' en 'data' de la respuesta JSON sea 1.
                .andExpect(jsonPath("$.data.id").value(1))
                // Verifica que el campo 'name' en 'data' de la respuesta JSON sea "John Doe".
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                // Verifica que el campo 'role' en 'data' de la respuesta JSON sea "ADMIN".
                .andExpect(jsonPath("$.data.role").value("ADMIN"))
                // Verifica que el campo 'token' en 'data' de la respuesta JSON sea "anytoken".
                .andExpect(jsonPath("$.data.token").value("anytoken"));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        // Simula que el método 'findByEmail' del 'userService' lanza una excepción 'UserNotFoundException'
        // cuando se le pasa el email "john.doe@example2.com".
        when(userService.findByEmail("john.doe@example2.com")).thenThrow(new UserNotFoundException("User not found"));
        // Realiza una solicitud POST a la URL "/api/login" con los parámetros 'email' y 'password'.
        mockMvc.perform(post("/api/login")
                        .param("email", "john.doe@example2.com")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON))
                // Verifica que el estado de la respuesta sea 404 (Not Found).
                .andExpect(status().isNotFound())
                // Verifica que el código en la respuesta JSON sea 404.
                .andExpect(jsonPath("$.code").value(404))
                // Verifica que el mensaje en la respuesta JSON sea "User not found".
                .andExpect(jsonPath("$.message").value("User not found"))
                // Verifica que el campo 'data' en la respuesta JSON esté vacío.
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testLogin_InvalidPassword() throws Exception {
        // Simula que el método 'findByEmail' del 'userService' devuelve el objeto 'user' cuando se le pasa el email "john.doe@example.com".
        when(userService.findByEmail("john.doe@example.com")).thenReturn(user);
        // Simula que el método 'matches' del 'bCryptPasswordEncoder' devuelve 'false' cuando se le pasa el 'wrongPassword'
        // y el hash de la contraseña del usuario, indicando que la contraseña es incorrecta.
        when(bCryptPasswordEncoder.matches("wrongPassword", "$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe")).thenReturn(false);
        // Realiza una solicitud POST a la URL "/api/login" con los parámetros 'email' y 'password'.
        mockMvc.perform(post("/api/login")
                        .param("email", "john.doe@example.com")
                        .param("password", "wrongPassword")
                        .contentType(MediaType.APPLICATION_JSON))
                // Verifica que el estado de la respuesta sea 403 (Forbidden).
                .andExpect(status().isForbidden())
                // Verifica que el código en la respuesta JSON sea 403.
                .andExpect(jsonPath("$.code").value(403))
                // Verifica que el mensaje en la respuesta JSON sea "Invalid password for email john.doe@example.com".
                .andExpect(jsonPath("$.message").value("Invalid password for email john.doe@example.com"))
                // Verifica que el campo 'data' en la respuesta JSON esté vacío.
                .andExpect(jsonPath("$.data").isEmpty());
    }

}