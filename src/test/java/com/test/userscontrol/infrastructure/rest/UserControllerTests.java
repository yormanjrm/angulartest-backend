package com.test.userscontrol.infrastructure.rest;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.exception.DuplicateUserException;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTests {
    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testRegisterUser() throws Exception {
        // Crea un objeto User para usar en la prueba
        User user = new User(0, "Mark", "mark.doe@example.com", "password123", "RECEP", "null", null);
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para devolver el objeto User cuando se llama al método save
        when(userService.save(any(User.class), any())).thenReturn(user);
        // Realiza una solicitud POST a la URL /api/users/register con los parámetros necesarios
        mockMvc.perform(post("/api/users/register")
                        .param("id", "0")
                        .param("name", "Mark")
                        .param("email", "mark.doe@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "null")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 201 Created
                .andExpect(status().isCreated())
                // Verifica que el campo "email" en la respuesta sea "mark.doe@example.com"
                .andExpect(jsonPath("$.data.email").value("mark.doe@example.com"));
    }

    @Test
    void testRegisterUser_DuplicateUser() throws Exception {
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para lanzar una DuplicateUserException cuando se llama al método save
        when(userService.save(any(User.class), any())).thenThrow(new DuplicateUserException("User already exists"));
        // Realiza una solicitud POST a la URL /api/users/register con los parámetros necesarios
        mockMvc.perform(post("/api/users/register")
                        .param("id", "0")
                        .param("name", "Mark")
                        .param("email", "mark.doe@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "null")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 409 Conflict
                .andExpect(status().isConflict())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "User already exists"
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void testRegisterUser_InternalServerError() throws Exception {
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para lanzar una RuntimeException cuando se llama al método save
        when(userService.save(any(User.class), any())).thenThrow(new RuntimeException("Internal server error"));
        // Realiza una solicitud POST a la URL /api/users/register con los parámetros necesarios
        mockMvc.perform(post("/api/users/register")
                        .param("id", "0")
                        .param("name", "Mark")
                        .param("email", "mark.doe@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "null")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(status().isInternalServerError())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "Internal server error"
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }

    @Test
    void testFindAllUsers() throws Exception {
        // Configura el mock para devolver una lista de usuarios cuando se llama al método findAll
        when(userService.findAll()).thenReturn(List.of(new User()));
        // Realiza una solicitud GET a la URL /api/users/get/all
        mockMvc.perform(get("/api/users/get/all"))
                // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(status().isOk())
                // Verifica que el campo "data" en la respuesta sea un array
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testFindAllUsers_InternalServerError() throws Exception {
        // Configura el mock para lanzar una RuntimeException cuando se llama al método findAll
        when(userService.findAll()).thenThrow(new RuntimeException("Internal server error"));
        // Realiza una solicitud GET a la URL /api/users/get/all
        mockMvc.perform(get("/api/users/get/all"))
                // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(status().isInternalServerError())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "Internal server errror, try again."
                .andExpect(jsonPath("$.message").value("Internal server errror, try again."));
    }

    @Test
    void testFindUserById() throws Exception {
        // Crea un objeto User para usar en la prueba
        User user = new User(3, "Mark", "mark@example.com", "password123", "RECEP", "http://localhost:8080/images/default.png", null);
        // Configura el mock para devolver el objeto User cuando se llama al método findById
        when(userService.findById(anyInt())).thenReturn(user);
        // Realiza una solicitud GET a la URL /api/users/get/byId con el parámetro id igual a 3
        mockMvc.perform(get("/api/users/get/byId")
                        .param("id", "3"))
                // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(status().isOk())
                // Verifica que el campo "email" en la respuesta sea "mark@example.com"
                .andExpect(jsonPath("$.data.email").value("mark@example.com"));
    }

    @Test
    void testFindUserById_InternalServerError() throws Exception {
        // Configura el mock para lanzar una RuntimeException cuando se llama al método findById
        when(userService.findById(anyInt())).thenThrow(new RuntimeException("Internal server error"));
        // Realiza una solicitud GET a la URL /api/users/get/byId con el parámetro id igual a 3
        mockMvc.perform(get("/api/users/get/byId")
                        .param("id", "3"))
                // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(status().isInternalServerError())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "Internal server errror, try again."
                .andExpect(jsonPath("$.message").value("Internal server errror, try again."));
    }

    @Test
    void testUpdateUser() throws Exception {
        // Crea un objeto User para usar en la prueba
        User user = new User(3, "Mark", "mark@example.com", "password123", "RECEP", "http://localhost:8080/images/default.png", null);
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para devolver el objeto User cuando se llama al método updateUser
        when(userService.updateUser(any(User.class), any())).thenReturn(user);
        // Realiza una solicitud PUT a la URL /api/users/update con los parámetros necesarios
        mockMvc.perform(put("/api/users/update")
                        .param("id", "3")
                        .param("name", "Mark")
                        .param("email", "mark@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "http://localhost:8080/images/default.png")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(status().isOk())
                // Verifica que el campo "email" en la respuesta sea "mark@example.com"
                .andExpect(jsonPath("$.data.email").value("mark@example.com"));
    }

    @Test
    void testUpdateUser_UserNotFound() throws Exception {
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para lanzar una UserNotFoundException cuando se llama al método updateUser
        when(userService.updateUser(any(User.class), any())).thenThrow(new UserNotFoundException("User not found"));
        // Realiza una solicitud PUT a la URL /api/users/update con los parámetros necesarios
        mockMvc.perform(put("/api/users/update")
                        .param("id", "3")
                        .param("name", "Mark")
                        .param("email", "mark@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "http://localhost:8080/images/default.png")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 404 Not Found
                .andExpect(status().isNotFound())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "User not found"
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testUpdateUser_InternalServerError() throws Exception {
        // Configura el mock para devolver una contraseña codificada cuando se llama al método encode
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        // Configura el mock para lanzar una RuntimeException cuando se llama al método updateUser
        when(userService.updateUser(any(User.class), any())).thenThrow(new RuntimeException("Internal server error"));
        // Realiza una solicitud PUT a la URL /api/users/update con los parámetros necesarios
        mockMvc.perform(put("/api/users/update")
                        .param("id", "3")
                        .param("name", "Mark")
                        .param("email", "mark@example.com")
                        .param("password", "password123")
                        .param("role", "RECEP")
                        .param("image", "http://localhost:8080/images/default.png")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(status().isInternalServerError())
                // Verifica que el campo "error" en la respuesta sea true
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "Internal server error"
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Configura el mock para no hacer nada cuando se llama al método deleteById con cualquier entero.
        doNothing().when(userService).deleteById(anyInt());
        // Realiza una solicitud DELETE a la URL /api/users/delete con el parámetro id igual a 3.
        mockMvc.perform(delete("/api/users/delete")
                        .param("id", "3"))
                // Verifica que el estado de la respuesta sea 200 OK.
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_UserNotFound() throws Exception {
        // Configura el mock para lanzar una UserNotFoundException cuando se llama al método deleteById con cualquier entero.
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteById(anyInt());
        // Realiza una solicitud DELETE a la URL /api/users/delete con el parámetro id igual a 3.
        mockMvc.perform(delete("/api/users/delete")
                        .param("id", "3"))
                // Verifica que el estado de la respuesta sea 404 Not Found.
                .andExpect(status().isNotFound())
                // Verifica que el campo "error" en la respuesta sea true.
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "User not found".
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testDeleteUser_InternalServerError() throws Exception {
        // Configura el mock para lanzar una RuntimeException cuando se llama al método deleteById con cualquier entero.
        doThrow(new RuntimeException("Internal server error")).when(userService).deleteById(anyInt());
        // Realiza una solicitud DELETE a la URL /api/users/delete con el parámetro id igual a 3.
        mockMvc.perform(delete("/api/users/delete")
                        .param("id", "3"))
                // Verifica que el estado de la respuesta sea 500 Internal Server Error.
                .andExpect(status().isInternalServerError())
                // Verifica que el campo "error" en la respuesta sea true.
                .andExpect(jsonPath("$.error").value(true))
                // Verifica que el campo "message" en la respuesta contenga el mensaje "Internal server error".
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }

}