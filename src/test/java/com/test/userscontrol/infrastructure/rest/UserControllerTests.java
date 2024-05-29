package com.test.userscontrol.infrastructure.rest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

public class UserControllerTests {

    private MockMvc mockMvc;
    private LocalDateTime now;
    private User newuser;
    private User user1;
    private User user2;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        now = LocalDateTime.now();
        newuser = new User(null, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", null);
        user1 = new User(1, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", now);
        user2 = new User(2, "Jane Doe", "jane.doe@example.com", "password123", "USER", "default.png", now);
    }

    @Test
    public void shouldSaveUser() throws Exception {
        // Configura el comportamiento esperado del método 'save()' del mock 'userService'
        // Cuando se llama al método 'save()' con cualquier instancia de 'User', devuelve 'user1'
        when(userService.save(any(User.class))).thenReturn(user1);
        // Realiza una solicitud HTTP POST al endpoint '/api/users/register'
        mockMvc.perform(post("/api/users/register")
                        // Establece el tipo de contenido de la solicitud como JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        // Configura el cuerpo de la solicitud como la representación JSON de 'newuser'
                        .content(new ObjectMapper().writeValueAsString(newuser)))
                // Verifica que el estado de la respuesta sea 201 (CREATED)
                .andExpect(status().isCreated())
                // Verifica que el cuerpo de la respuesta tenga el campo 'id' con el valor de 'user1.getId()'
                .andExpect(jsonPath("$.id").value(user1.getId()))
                // Verifica que el cuerpo de la respuesta tenga el campo 'name' con el valor de 'user1.getName()'
                .andExpect(jsonPath("$.name").value(user1.getName()))
                // Verifica que el cuerpo de la respuesta tenga el campo 'email' con el valor de 'user1.getEmail()'
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                // Verifica que el cuerpo de la respuesta tenga el campo 'password' con el valor de 'user1.getPassword()'
                .andExpect(jsonPath("$.password").value(user1.getPassword()))
                // Verifica que el cuerpo de la respuesta tenga el campo 'role' con el valor de 'user1.getRole()'
                .andExpect(jsonPath("$.role").value(user1.getRole()))
                // Verifica que el cuerpo de la respuesta tenga el campo 'image' con el valor de 'user1.getImage()'
                .andExpect(jsonPath("$.image").value(user1.getImage()));
        // Verifica que el método 'save()' del mock 'userService' se haya llamado exactamente una vez
        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void shouldReturnAllUsers() throws Exception {
        // Configura el comportamiento esperado del método 'findAll()' del mock 'userService'
        // Cuando se llama al método 'findAll()', devuelve una lista que contiene 'user1' y 'user2'
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));
        // Realiza una solicitud HTTP GET al endpoint '/api/users/get-all'
        mockMvc.perform(get("/api/users/get-all")
                        // Establece el tipo de contenido de la solicitud como JSON
                        .contentType(MediaType.APPLICATION_JSON))
                // Verifica que el estado de la respuesta sea 200 (OK)
                .andExpect(status().isOk())
                // Verifica que el primer elemento del arreglo tenga el campo 'id' con el valor de 'user1.getId()'
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                // Verifica que el primer elemento del arreglo tenga el campo 'name' con el valor de 'user1.getName()'
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                // Verifica que el primer elemento del arreglo tenga el campo 'email' con el valor de 'user1.getEmail()'
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                // Verifica que el primer elemento del arreglo tenga el campo 'password' con el valor de 'user1.getPassword()'
                .andExpect(jsonPath("$[0].password").value(user1.getPassword()))
                // Verifica que el primer elemento del arreglo tenga el campo 'role' con el valor de 'user1.getRole()'
                .andExpect(jsonPath("$[0].role").value(user1.getRole()))
                // Verifica que el primer elemento del arreglo tenga el campo 'image' con el valor de 'user1.getImage()'
                .andExpect(jsonPath("$[0].image").value(user1.getImage()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'id' con el valor de 'user2.getId()'
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'name' con el valor de 'user2.getName()'
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'email' con el valor de 'user2.getEmail()'
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'password' con el valor de 'user2.getPassword()'
                .andExpect(jsonPath("$[1].password").value(user2.getPassword()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'role' con el valor de 'user2.getRole()'
                .andExpect(jsonPath("$[1].role").value(user2.getRole()))
                // Verifica que el segundo elemento del arreglo tenga el campo 'image' con el valor de 'user2.getImage()'
                .andExpect(jsonPath("$[1].image").value(user2.getImage()));
        // Verifica que el método 'findAll()' del mock 'userService' se haya llamado exactamente una vez
        verify(userService, times(1)).findAll();

    }

    @Test
    public void shouldReturnAnUserFindedById() throws Exception {
        // Configuramos el comportamiento esperado del servicio al llamar a findById con el id 1
        when(userService.findById(1)).thenReturn(user1);
        // Simulamos una solicitud GET con el id proporcionado como form-data
        mockMvc.perform(get("/api/users/get-byId")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        // Aquí se agrega el id como parámetro del formulario
                        .param("id", "1"))
                // Verifica que el código de estado de la respuesta sea 200 OK
                .andExpect(status().isOk())
                // Verifica que el id del usuario en la respuesta coincida con user1.getId()
                .andExpect(jsonPath("$.id").value(user1.getId()))
                // Verifica que el nombre del usuario en la respuesta coincida con user1.getName()
                .andExpect(jsonPath("$.name").value(user1.getName()))
                // Verifica que el email del usuario en la respuesta coincida con user1.getEmail()
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                // Verifica que el password del usuario en la respuesta coincida con user1.getPassword()
                .andExpect(jsonPath("$.password").value(user1.getPassword()))
                // Verifica que el rol del usuario en la respuesta coincida con user1.getRole()
                .andExpect(jsonPath("$.role").value(user1.getRole()))
                // Verifica que la imagen del usuario en la respuesta coincida con user1.getImage()
                .andExpect(jsonPath("$.image").value(user1.getImage()));
        // Verificamos que el método findById del servicio se haya llamado exactamente una vez con el id proporcionado
        verify(userService, times(1)).findById(1);
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        // ID del usuario que quieres eliminar
        Integer userIdToDelete = 1;
        // Configurar el comportamiento esperado del servicio userService
        doNothing().when(userService).deleteById(userIdToDelete);
        // Realizar una solicitud DELETE al endpoint "/api/users/delete" con el ID proporcionado como parámetro
        mockMvc.perform(delete("/api/users/delete")
                        .param("id", userIdToDelete.toString()))
                // Verificar que la solicitud se completó exitosamente (200 OK)
                .andExpect(status().isOk());
        // Verificar que se llamó al método deleteById del servicio userService con el ID proporcionado
        verify(userService, times(1)).deleteById(userIdToDelete);
    }

}